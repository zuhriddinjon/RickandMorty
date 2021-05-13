package uz.instat.rickandmorty.data.local

object LocalInjector {

    var appDatabase: AppDataBase? = null

    fun injectDb(): AppDataBase? {
        return appDatabase
    }
}