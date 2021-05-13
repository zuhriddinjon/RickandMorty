package uz.instat.rickandmorty

import android.app.Application
import android.content.Context
import uz.instat.rickandmorty.data.local.DataBaseProvider
import uz.instat.rickandmorty.data.local.LocalInjector
import uz.instat.rickandmorty.data.remote.NetworkManager
import uz.instat.rickandmorty.data.remote.RemoteInjector

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        LocalInjector.appDatabase = DataBaseProvider.getInstance(this)
        RemoteInjector.apiService = NetworkManager.getInstance()
    }

    companion object {
        var context: Context? = null
    }
}