package uz.instat.rickandmorty.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uz.instat.rickandmorty.data.model.character.CharacterList
import uz.instat.rickandmorty.data.model.character.CharacterModel
import uz.instat.rickandmorty.data.model.episode.EpisodeList
import uz.instat.rickandmorty.data.model.episode.EpisodeModel
import uz.instat.rickandmorty.data.model.location.LocationList
import uz.instat.rickandmorty.data.model.location.LocationModel

interface ApiService {

    @GET("character")
    suspend fun getAllCharactersInPage(@Query("page") page:Int): Response<CharacterList>

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Long): Response<CharacterModel>

    @GET("location")
    suspend fun getAllLocationsInPage(@Query("page") page:Int): Response<LocationList>

    @GET("location/{id}")
    suspend fun getLocation(@Path("id") id: Long): Response<LocationModel>

    @GET("episode")
    suspend fun getAllEpisodesInPage(@Query("page") page:Int): Response<EpisodeList>

    @GET("episode/{id}")
    suspend fun getEpisode(@Path("id") id: Long): Response<EpisodeModel>

}