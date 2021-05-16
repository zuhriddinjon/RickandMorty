package uz.instat.rickandmorty.data.model.character

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "character")
@Parcelize
data class CharacterModel(
    @PrimaryKey
    val id: Long,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Origin,
    val location: Location,
    val image: String,
    val episode: List<String>,
    val created: String,
    val url: String
): Parcelable