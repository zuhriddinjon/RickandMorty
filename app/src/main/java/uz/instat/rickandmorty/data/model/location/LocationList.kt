package uz.instat.rickandmorty.data.model.location

import uz.instat.rickandmorty.data.model.Info

data class LocationList(
    val info: Info,
    val results: List<LocationModel>
)