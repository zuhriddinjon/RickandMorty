package uz.instat.rickandmorty.repo.charakter

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import uz.instat.rickandmorty.common.Constants
import uz.instat.rickandmorty.common.nextKey
import uz.instat.rickandmorty.common.prevKey
import uz.instat.rickandmorty.data.local.AppDataBase
import uz.instat.rickandmorty.data.model.character.CharacterKeys
import uz.instat.rickandmorty.data.model.character.CharacterModel
import uz.instat.rickandmorty.data.remote.ApiService
import java.io.InvalidObjectException

@ExperimentalPagingApi
class CharacterMediator(private val apiService: ApiService, private val appDatabase: AppDataBase) :
    RemoteMediator<Int, CharacterModel>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterModel>
    ): MediatorResult {

        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            var result = emptyList<CharacterModel>()
            var prevKey: Int? = null
            var nextKey: Int? = null
            val response = apiService.getAllCharactersInPage(page)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                result = body.results
                prevKey = body.info.prevKey
                nextKey = body.info.nextKey
            }
            val isEndOfList = nextKey == null
            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.characterKeysDao().clearCharacterKeys()
                    appDatabase.characterDao().clearCharacter()
                }
                val keys = result.map {
                    CharacterKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                appDatabase.characterKeysDao().insertAll(keys)
                appDatabase.characterDao().insertAll(result)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, CharacterModel>
    ): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosesRemoteKey(state)
                remoteKeys?.prevKey?.plus(1) ?: Constants.DEFAULT_CHARACTERS_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                    ?: return Constants.DEFAULT_CHARACTERS_PAGE_INDEX
                //throw InvalidObjectException("Invalid state, key should not be null")
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                    ?: throw InvalidObjectException("Remote key should not be null for $loadType")
                remoteKeys.nextKey
            }
            else -> {
            }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, CharacterModel>): CharacterKeys? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { character -> appDatabase.characterKeysDao().remoteKeysCharacterId(character.id) }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, CharacterModel>): CharacterKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { character -> appDatabase.characterKeysDao().remoteKeysCharacterId(character.id) }
    }

    private suspend fun getClosesRemoteKey(state: PagingState<Int, CharacterModel>): CharacterKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                appDatabase.characterKeysDao().remoteKeysCharacterId(repoId)
            }
        }
    }
}