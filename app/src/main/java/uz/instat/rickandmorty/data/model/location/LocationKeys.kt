package uz.instat.rickandmorty.data.model.location

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_keys")
data class LocationKeys(
    @PrimaryKey val repoId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)
