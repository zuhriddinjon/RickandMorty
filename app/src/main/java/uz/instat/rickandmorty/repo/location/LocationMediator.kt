package uz.instat.rickandmorty.repo.location

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import uz.instat.rickandmorty.common.Constants
import uz.instat.rickandmorty.common.nextKey
import uz.instat.rickandmorty.common.prevKey
import uz.instat.rickandmorty.data.local.AppDataBase
import uz.instat.rickandmorty.data.model.location.LocationKeys
import uz.instat.rickandmorty.data.model.location.LocationModel
import uz.instat.rickandmorty.data.remote.ApiService

@OptIn(ExperimentalPagingApi::class)
class LocationMediator(
    private val apiService: ApiService,
    private val appDatabase: AppDataBase
) :
    RemoteMediator<Int, LocationModel>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocationModel>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosesRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: Constants.DEFAULT_LOCATIONS_PAGE_INDEX
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
            var result = emptyList<LocationModel>()
            var prevKey: Int? = null
            var nextKey: Int? = null
            val response = apiService.getAllLocationsInPage(page)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                result = body.results
                prevKey = body.info.prevKey
                nextKey = body.info.nextKey
            }
            val isEndOfList = nextKey == null
            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.locationKeysDao().clearLocationKeys()
                    appDatabase.locationDao().clearLocation()
                }
                val keys = result.map {
                    LocationKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                appDatabase.locationKeysDao().insertAll(keys)
                appDatabase.locationDao().insertAll(result)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, LocationModel>): LocationKeys? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { location -> appDatabase.locationKeysDao().remoteKeysLocationId(location.id) }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, LocationModel>): LocationKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { location -> appDatabase.locationKeysDao().remoteKeysLocationId(location.id) }
    }

    private suspend fun getClosesRemoteKey(state: PagingState<Int, LocationModel>): LocationKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                appDatabase.locationKeysDao().remoteKeysLocationId(repoId)
            }
        }
    }
}