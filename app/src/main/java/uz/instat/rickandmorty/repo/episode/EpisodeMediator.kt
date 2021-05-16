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

@OptIn(ExperimentalPagingApi::class)
class EpisodeMediator(
    private val apiService: ApiService,
    private val appDatabase: AppDataBase
) : RemoteMediator<Int, EpisodeModel>() {
    private val episodeDao = appDatabase.episodeDao()
    private val keyDao = appDatabase.episodeKeysDao()


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EpisodeModel>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosesRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: Constants.DEFAULT_EPISODES_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
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
                    keyDao.clearEpisodeKeys()
                    episodeDao.clearEpisode()
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