package uz.instat.rickandmorty.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.instat.rickandmorty.data.model.character.CharacterKeys

@Dao
interface CharacterKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characterKey: List<CharacterKeys>)

    @Query("select * from character_keys where repoId = :id")
    suspend fun remoteKeysCharacterId(id: Long): CharacterKeys?

    @Query("delete from character_keys")
    suspend fun clearCharacterKeys()
}