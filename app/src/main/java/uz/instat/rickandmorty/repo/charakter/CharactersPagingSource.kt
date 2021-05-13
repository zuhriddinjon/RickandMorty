package uz.instat.rickandmorty.repo.charakter

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.instat.rickandmorty.common.Constants
import uz.instat.rickandmorty.common.nextKey
import uz.instat.rickandmorty.common.prevKey
import uz.instat.rickandmorty.data.model.character.CharacterModel
import uz.instat.rickandmorty.data.remote.ApiService

@ExperimentalPagingApi
class CharactersPagingSource(private val apiService: ApiService) :
    PagingSource<Int, CharacterModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterModel> {
        val page = params.key ?: Constants.DEFAULT_CHARACTERS_PAGE_INDEX
        var nextKey: Int? = null
        var prevKey: Int? = null

        return try {
            var result: List<CharacterModel> = emptyList()
            val response = apiService.getAllCharactersInPage(page)
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

    override fun getRefreshKey(state: PagingState<Int, CharacterModel>): Int? {
        return null
    }
}