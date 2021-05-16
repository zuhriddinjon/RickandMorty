package uz.instat.rickandmorty.ui.episode

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

    private lateinit var _episodesFlow: Flow<PagingData<EpisodeModel>>
    val episodesFlow: Flow<PagingData<EpisodeModel>>
        get() = _episodesFlow

    init {
        fetchEpisodes()
    }

    private fun fetchEpisodes() {
        _episodesFlow = try {
            repo.getEpisodesFlowDb().cachedIn(viewModelScope)
        } catch (e: Exception) {
            flowOf(PagingData.empty())
        }
    }
}