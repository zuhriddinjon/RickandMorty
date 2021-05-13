package uz.instat.rickandmorty.data.remote

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.instat.rickandmorty.App
import uz.instat.rickandmorty.common.Constants
import java.util.concurrent.TimeUnit

class NetworkManager {

    companion object {
        private var instance: ApiService? = null
        fun getInstance(): ApiService {
            if (instance == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpclient())
                    .build()
                instance = retrofit.create(ApiService::class.java)
            }
            return instance!!
        }

        private fun okhttpclient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(
                    ChuckerInterceptor.Builder(App.context!!)
                        .collector(ChuckerCollector(App.context!!))
                        .maxContentLength(250000L)
                        .redactHeaders(emptySet())
                        .alwaysReadResponseBody(false)
                        .build()
                )
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build()
        }

    }
}