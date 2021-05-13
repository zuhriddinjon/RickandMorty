package uz.instat.rickandmorty.data.model.character

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_keys")
data class CharacterKeys(
    @PrimaryKey val repoId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)
