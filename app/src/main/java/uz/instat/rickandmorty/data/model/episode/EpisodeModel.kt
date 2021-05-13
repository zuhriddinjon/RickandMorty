package uz.instat.rickandmorty.data.model.episode

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "episode")
data class EpisodeModel(
    @PrimaryKey
    val id: Long,
    val name: String,
    @SerializedName("air_date")
    @ColumnInfo(name = "air_date")
    val airDate: String,
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String
)