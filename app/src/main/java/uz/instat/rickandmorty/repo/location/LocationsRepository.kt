package uz.instat.rickandmorty.repo.location

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.instat.rickandmorty.common.Constants
import uz.instat.rickandmorty.data.local.AppDataBase
import uz.instat.rickandmorty.data.local.LocalInjector
import uz.instat.rickandmorty.data.model.location.LocationModel
import uz.instat.rickandmorty.data.remote.ApiService
import uz.instat.rickandmorty.data.remote.RemoteInjector

class LocationsRepository(
    private val apiService: ApiService = RemoteInjector.injectApiService()!!,
    private val appDataBase: AppDataBase? = LocalInjector.injectDb()
) {

    companion object {
        fun getInstance() = LocationsRepository()
    }

    @ExperimentalPagingApi
    fun getLocationsFlow(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<LocationModel>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { LocationsPagingSource(apiService) }
        ).flow
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(
            pageSize = Constants.DEFAULT_LOCATIONS_PAGE_SIZE,
            prefetchDistance = 1,
            enablePlaceholders = true
        )
    }

    @ExperimentalPagingApi
    fun getLocationsFlowDb(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<LocationModel>> {
        if (appDataBase == null) throw IllegalStateException("Database is not initialized")

        val pagingSourceFactory = { appDataBase.locationDao().getAllLocation() }
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = LocationMediator(apiService, appDataBase)
        ).flow
    }

    fun getLocationFlowDb(id: Long): Flow<LocationModel> {
        if (appDataBase == null) throw IllegalStateException("Database is not initialized")

        return flow {
            var location: LocationModel? = appDataBase.locationDao().getLocation(id)
            if (location == null) {
                val res = apiService.getLocation(id)
                if (res.isSuccessful && res.body() != null) {
                    location = res.body()
                    appDataBase.locationDao().insert(location!!)
                }
            }
            emit(location!!)
        }
    }

    fun getCharactersInLocation(ids: List<Long>): Flow<List<LocationModel>> {
        if (appDataBase == null) throw IllegalStateException("Database is not initialized")

        return flow {
            val locationList = mutableListOf<LocationModel>()
            ids.forEach { id ->
                var location: LocationModel? = appDataBase.locationDao().getLocation(id)
                if (location == null) {
                    val res = apiService.getLocation(id)
                    if (res.isSuccessful && res.body() != null) {
                        location = res.body()
                        appDataBase.locationDao().insert(location!!)
                    }
                }
                locationList.add(location!!)
            }
            emit(locationList)
        }
    }
}

