package it.unical.gciaoo.vinteddu_android.ApiConfig

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        private const val BASE_URL = "http://192.168.43.254:8080/" // Mettere l'indirizzo ip del pc tra // e :
//192.168.1.54    192.168.1.8     http://192.168.1.54:8080/  http:192.168.1.54//:8080   192.168.43.254
        fun create(sessionManager: SessionManager): ApiService {
            val interceptor = AuthInterceptor(sessionManager)
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}
