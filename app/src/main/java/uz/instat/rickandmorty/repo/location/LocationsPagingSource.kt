package uz.instat.rickandmorty.repo.location

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.instat.rickandmorty.common.Constants
import uz.instat.rickandmorty.common.nextKey
import uz.instat.rickandmorty.common.prevKey
import uz.instat.rickandmorty.data.model.location.LocationModel
import uz.instat.rickandmorty.data.remote.ApiService

@ExperimentalPagingApi
class LocationsPagingSource(private val apiService: ApiService) :
    PagingSource<Int, LocationModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocationModel> {
        val page = params.key ?: Constants.DEFAULT_LOCATIONS_PAGE_INDEX
        var nextKey: Int? = null
        var prevKey: Int? = null

        return try {
            var result: List<LocationModel> = emptyList()
            val response = apiService.getAllLocationsInPage(page)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                result = body.results
                nextKey = body.info.nextKey
                prevKey = body.info.prevKey
            }

            LoadResult.Page(
                data = result, prevKey = nextKey,
                nextKey = prevKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LocationModel>): Int? {
        return null
    }
}