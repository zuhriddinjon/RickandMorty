package uz.instat.rickandmorty.data.model.episode

import uz.instat.rickandmorty.data.model.Info

data class EpisodeList(
    val info: Info,
    val results: List<EpisodeModel>
)