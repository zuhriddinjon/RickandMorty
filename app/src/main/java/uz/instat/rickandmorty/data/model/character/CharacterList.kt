package uz.instat.rickandmorty.data.model.character

import uz.instat.rickandmorty.data.model.Info

data class CharacterList(
    val info: Info,
    val results: List<CharacterModel>
)