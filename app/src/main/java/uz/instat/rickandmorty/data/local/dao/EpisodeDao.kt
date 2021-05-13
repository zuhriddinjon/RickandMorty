package uz.instat.rickandmorty.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.instat.rickandmorty.data.model.episode.EpisodeModel

@Dao
interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(episode: EpisodeModel): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodes: List<EpisodeModel>)

    @Query("select * from episode")
    fun getAllEpisode(): PagingSource<Int, EpisodeModel>

    @Query("select * from episode where id=:id")
    suspend fun getEpisode(id: Long): EpisodeModel?

    @Query("delete from episode")
    suspend fun clearEpisode()
}