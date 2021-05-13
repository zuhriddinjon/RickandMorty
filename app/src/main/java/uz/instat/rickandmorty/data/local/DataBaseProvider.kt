package uz.instat.rickandmorty.data.local

import android.content.Context
import androidx.room.Room

object DataBaseProvider {
    fun getInstance(context: Context): AppDataBase {
        return Room.databaseBuilder(context, AppDataBase::class.java, "rickandmorty.db")
            .build()
    }
}