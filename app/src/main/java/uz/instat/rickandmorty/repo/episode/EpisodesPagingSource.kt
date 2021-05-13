package uz.instat.rickandmorty.repo.episode

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.instat.rickandmorty.common.Constants
import uz.instat.rickandmorty.common.nextKey
import uz.instat.rickandmorty.common.prevKey
import uz.instat.rickandmorty.data.model.episode.EpisodeModel
import uz.instat.rickandmorty.data.remote.ApiService

@ExperimentalPagingApi
class EpisodesPagingSource(private val apiService: ApiService) :
    PagingSource<Int, EpisodeModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodeModel> {
        val page = params.key ?: Constants.DEFAULT_EPISODES_PAGE_INDEX
        var nextKey: Int? = null
        var prevKey: Int? = null

        return try {
            var result: List<EpisodeModel> = emptyList()
            val response = apiService.getAllEpisodesInPage(page)
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

    override fun getRefreshKey(state: PagingState<Int, EpisodeModel>): Int? {
        return null
    }
}