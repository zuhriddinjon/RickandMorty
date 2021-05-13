package uz.instat.rickandmorty.data.remote

object RemoteInjector {

    var apiService: ApiService? = null

    fun injectApiService(): ApiService? {
        return apiService
    }
}