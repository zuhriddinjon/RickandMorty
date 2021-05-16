package uz.instat.rickandmorty.data.model.character

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Origin(
    val name: String,
    val url: String
):Parcelable