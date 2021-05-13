package uz.instat.rickandmorty.ui.episode

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import uz.instat.rickandmorty.data.model.episode.EpisodeModel
import uz.instat.rickandmorty.repo.episode.EpisodesRepository

@ExperimentalPagingApi
class EpisodesViewModel(
    private val repo: EpisodesRepository = EpisodesRepository.getInstance()
) : ViewModel() {
    fun fetchEpisodes(): Flow<PagingData<EpisodeModel>> {
        return try {
            repo.getEpisodesFlowDb().cachedIn(viewModelScope)
        } catch (e: Exception) {
            Log.d("TAG", "fetchCharacters: ${e.message}")
            flowOf(PagingData.empty())
        }
    }
}