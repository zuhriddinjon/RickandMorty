package uz.instat.rickandmorty.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uz.instat.rickandmorty.data.local.converter.LocationConverter
import uz.instat.rickandmorty.data.local.converter.OriginConverter
import uz.instat.rickandmorty.data.local.converter.StringListConverter
import uz.instat.rickandmorty.data.local.dao.*
import uz.instat.rickandmorty.data.model.character.CharacterKeys
import uz.instat.rickandmorty.data.model.character.CharacterModel
import uz.instat.rickandmorty.data.model.episode.EpisodeKeys
import uz.instat.rickandmorty.data.model.episode.EpisodeModel
import uz.instat.rickandmorty.data.model.location.LocationKeys
import uz.instat.rickandmorty.data.model.location.LocationModel

@Database(
    entities = [CharacterModel::class, EpisodeModel::class, LocationModel::class,
        CharacterKeys::class, EpisodeKeys::class, LocationKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(value = [StringListConverter::class, OriginConverter::class, LocationConverter::class])
abstract class AppDataBase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun locationDao(): LocationDao
    abstract fun characterKeysDao(): CharacterKeysDao
    abstract fun episodeKeysDao(): EpisodeKeysDao
    abstract fun locationKeysDao(): LocationKeysDao
}