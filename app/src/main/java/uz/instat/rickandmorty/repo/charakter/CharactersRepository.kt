package uz.instat.rickandmorty.repo.charakter

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.instat.rickandmorty.common.Constants
import uz.instat.rickandmorty.data.local.AppDataBase
import uz.instat.rickandmorty.data.local.LocalInjector
import uz.instat.rickandmorty.data.model.character.CharacterModel
import uz.instat.rickandmorty.data.model.episode.EpisodeModel
import uz.instat.rickandmorty.data.remote.ApiService
import uz.instat.rickandmorty.data.remote.RemoteInjector

class CharactersRepository(
    private val apiService: ApiService = RemoteInjector.injectApiService()!!,
    private val appDataBase: AppDataBase = LocalInjector.injectDb()!!
) {

    companion object {
        fun getInstance() = CharactersRepository()
    }

    @ExperimentalPagingApi
    fun getCharactersFlow(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<CharacterModel>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { CharactersPagingSource(apiService) },
            remoteMediator = CharacterMediator(apiService, appDataBase)

        ).flow
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(
            pageSize = Constants.DEFAULT_CHARACTERS_PAGE_SIZE,
            prefetchDistance = 1,
            enablePlaceholders = true
        )
    }

    @ExperimentalPagingApi
    fun getCharactersFlowDb(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<CharacterModel>> {

        val pagingSourceFactory = { appDataBase.characterDao().getAllCharacter() }
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = CharacterMediator(apiService, appDataBase)
        ).flow
    }

    fun getCharacterFlowDb(id: Long): Flow<CharacterModel> {

        return flow {
            var character: CharacterModel? = appDataBase.characterDao().getCharacter(id)
            if (character == null) {
                val res = apiService.getCharacter(id)
                if (res.isSuccessful && res.body() != null) {
                    character = res.body()
                    appDataBase.characterDao().insert(character!!)
                }
            }
            emit(character!!)
        }
    }

    fun getEpisodesInCharacter(ids: List<Long>): Flow<List<EpisodeModel>> {

        return flow {
            val episodeList = mutableListOf<EpisodeModel>()
            ids.forEach { id ->
                var episode: EpisodeModel? = appDataBase.episodeDao().getEpisode(id)
                if (episode == null) {
                    val res = apiService.getEpisode(id)
                    if (res.isSuccessful && res.body() != null) {
                        episode = res.body()
                        appDataBase.episodeDao().insert(episode!!)
                    }
                }
                episodeList.add(episode!!)
            }
            emit(episodeList)
        }
    }
}

