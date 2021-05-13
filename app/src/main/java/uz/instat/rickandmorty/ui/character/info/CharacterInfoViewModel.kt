package uz.instat.rickandmorty.ui.character.info

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import uz.instat.rickandmorty.data.model.character.CharacterModel
import uz.instat.rickandmorty.repo.charakter.CharactersRepository

class CharacterInfoViewModel(
    private val repo: CharactersRepository = CharactersRepository.getInstance()
) : ViewModel() {

    fun fetchCharacter(id: Long): Flow<CharacterModel> {
        return try {
            repo.getCharacterFlowDb(id)
        } catch (e: Exception) {
            flowOf()
        }
    }
}