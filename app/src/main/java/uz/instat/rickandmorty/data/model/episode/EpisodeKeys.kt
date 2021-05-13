package uz.instat.rickandmorty.data.model.episode

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "episode_keys")
data class EpisodeKeys(
    @PrimaryKey val repoId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)
