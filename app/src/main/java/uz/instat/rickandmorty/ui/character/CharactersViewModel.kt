package uz.instat.rickandmorty.ui.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import uz.instat.rickandmorty.data.model.character.CharacterModel
import uz.instat.rickandmorty.repo.charakter.CharactersRepository

@ExperimentalPagingApi
class CharactersViewModel(
    private val repo: CharactersRepository = CharactersRepository.getInstance()
) : ViewModel() {

    fun fetchCharacters(): Flow<PagingData<CharacterModel>> {
        return try {
            repo.getCharactersFlowDb().cachedIn(viewModelScope)
        } catch (e: Exception) {
            flowOf(PagingData.empty())
        }
    }
}

