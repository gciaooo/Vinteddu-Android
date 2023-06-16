package it.unical.gciaoo.vinteddu_android
import it.unical.gciaoo.vinteddu_android.model.Item
import it.unical.gciaoo.vinteddu_android.model.User
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {


    @GET("/api/v2/user/{token}")
    suspend fun getCurrentUser(@Header("Authorization") token:String?, @Path("token") token_: String?) : Response<User>


    @GET("/api/v1/{userId}/Account")
    suspend fun getAccount(@Path("userId") userId: String): Response<User>
    @FormUrlEncoded
    @POST("/api/v1/authenticate")
    suspend fun authenticate(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<Unit>

    @GET("") //TODO: add api path
    suspend fun getItem(@Path("itemId") itemId: String): Response<Item>
}
