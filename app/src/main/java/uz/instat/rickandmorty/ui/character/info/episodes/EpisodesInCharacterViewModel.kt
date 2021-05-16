package uz.instat.rickandmorty.ui.character.info.episodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import uz.instat.rickandmorty.data.model.episode.EpisodeModel
import uz.instat.rickandmorty.repo.charakter.CharactersRepository

class EpisodesInCharacterViewModel(
    private val repo: CharactersRepository = CharactersRepository.getInstance()
) : ViewModel() {

    private lateinit var _episodesFlow: Flow<List<EpisodeModel>>
    val episodesFlow: Flow<List<EpisodeModel>>
        get() = _episodesFlow

    fun fetchEpisodesInCharacter(ids: List<Long>) {
        viewModelScope.launch {
            _episodesFlow = try {
                repo.getEpisodesInCharacter(ids)
            } catch (e: Exception) {
                flowOf()
            }
        }
    }
}