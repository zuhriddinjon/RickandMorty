package uz.instat.rickandmorty.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.instat.rickandmorty.data.model.episode.EpisodeKeys

@Dao
interface EpisodeKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodeKey: List<EpisodeKeys>)

    @Query("select * from episode_keys where repoId = :id")
    suspend fun remoteKeysEpisodeId(id: Long): EpisodeKeys?

    @Query("delete from episode_keys")
    suspend fun clearEpisodeKeys()
}