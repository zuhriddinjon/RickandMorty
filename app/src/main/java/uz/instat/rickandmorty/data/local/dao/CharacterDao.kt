package uz.instat.rickandmorty.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.instat.rickandmorty.data.model.character.CharacterModel

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: CharacterModel): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterModel>)

    @Query("select * from character")
    fun getAllCharacter(): PagingSource<Int, CharacterModel>

    @Query("select * from character where id=:id")
    suspend fun getCharacter(id: Long): CharacterModel?

    @Query("delete from character")
    suspend fun clearCharacter()
}