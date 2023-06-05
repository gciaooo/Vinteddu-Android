package it.unical.gciaoo.vinteddu_android
import it.unical.gciaoo.vinteddu_android.model.User
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Path

interface ApiService {


    @GET("/current")
    suspend fun getCurrentUserId() : Response<Long>


    @GET("/{userId}/Account")
    suspend fun getAccount(@Path("userId") userId: String): Response<User>
}
