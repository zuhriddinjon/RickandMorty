package uz.instat.rickandmorty.repo.episode

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import uz.instat.rickandmorty.common.Constants
import uz.instat.rickandmorty.common.nextKey
import uz.instat.rickandmorty.common.prevKey
import uz.instat.rickandmorty.data.local.AppDataBase
import uz.instat.rickandmorty.data.model.episode.EpisodeKeys
import uz.instat.rickandmorty.data.model.episode.EpisodeModel
import uz.instat.rickandmorty.data.remote.ApiService
import java.io.InvalidObjectException

@ExperimentalPagingApi
class EpisodeMediator(
    private val apiService: ApiService,
    private val appDatabase: AppDataBase
) :
    RemoteMediator<Int, EpisodeModel>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EpisodeModel>
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
            var result = emptyList<EpisodeModel>()
            var prevKey: Int? = null
            var nextKey: Int? = null
            val response = apiService.getAllEpisodesInPage(page)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                result = body.results
                prevKey = body.info.prevKey
                nextKey = body.info.nextKey
            }
            val isEndOfList = nextKey == null
            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.episodeKeysDao().clearEpisodeKeys()
                    appDatabase.episodeDao().clearEpisode()
                }
                val keys = result.map {
                    EpisodeKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                appDatabase.episodeKeysDao().insertAll(keys)
                appDatabase.episodeDao().insertAll(result)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, EpisodeModel>
    ): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosesRemoteKey(state)
                remoteKeys?.prevKey?.plus(1) ?: Constants.DEFAULT_EPISODES_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                    ?: return Constants.DEFAULT_EPISODES_PAGE_INDEX
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

    private suspend fun getFirstRemoteKey(state: PagingState<Int, EpisodeModel>): EpisodeKeys? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { episode -> appDatabase.episodeKeysDao().remoteKeysEpisodeId(episode.id) }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, EpisodeModel>): EpisodeKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { episode -> appDatabase.episodeKeysDao().remoteKeysEpisodeId(episode.id) }
    }

    private suspend fun getClosesRemoteKey(state: PagingState<Int, EpisodeModel>): EpisodeKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                appDatabase.episodeKeysDao().remoteKeysEpisodeId(repoId)
            }
        }
    }
}