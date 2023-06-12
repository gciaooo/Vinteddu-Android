package it.unical.gciaoo.vinteddu_android
import it.unical.gciaoo.vinteddu_android.model.User
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {


    @GET("/api/v1//users/{username}")
    suspend fun getCurrentUser(@Path("username") username: String?) : Response<User>


    @GET("/api/v1/{userId}/Account")
    suspend fun getAccount(@Path("userId") userId: String): Response<User>
    @FormUrlEncoded
    @POST("/api/v1/authenticate")
    suspend fun authenticate(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<Unit>

}
