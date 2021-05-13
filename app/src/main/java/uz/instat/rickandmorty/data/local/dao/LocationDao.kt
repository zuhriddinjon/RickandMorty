package uz.instat.rickandmorty.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.instat.rickandmorty.data.model.location.LocationModel

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationModel): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locations: List<LocationModel>)

    @Query("select * from location")
    fun getAllLocation(): PagingSource<Int, LocationModel>

    @Query("select * from location where id=:id")
    suspend fun getLocation(id: Long): LocationModel?

    @Query("delete from location")
    suspend fun clearLocation()

}