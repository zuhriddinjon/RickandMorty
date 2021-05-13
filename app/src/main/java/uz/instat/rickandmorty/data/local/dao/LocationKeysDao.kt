package uz.instat.rickandmorty.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.instat.rickandmorty.data.model.location.LocationKeys

@Dao
interface LocationKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locationKey: List<LocationKeys>)

    @Query("select * from location_keys where repoId = :id")
    suspend fun remoteKeysLocationId(id: Long): LocationKeys?

    @Query("delete from location_keys")
    suspend fun clearLocationKeys()
}