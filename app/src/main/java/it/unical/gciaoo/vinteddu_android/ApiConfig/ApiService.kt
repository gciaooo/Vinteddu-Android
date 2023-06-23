package it.unical.gciaoo.vinteddu_android.ApiConfig
import it.unical.gciaoo.vinteddu_android.model.Item
import it.unical.gciaoo.vinteddu_android.model.User
import it.unical.gciaoo.vinteddu_android.model.Utente
import it.unical.gciaoo.vinteddu_android.model.UtenteDTO
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {


    @GET("/api/v2/user/{token}")
    suspend fun getCurrentUser(@Header("Authorization") token:String?, @Path("token") token_: String?) : Response<UtenteDTO>


    @GET("/api/v1/{userId}/Account")
    suspend fun getAccount(@Path("userId") userId: String): Response<User>
    @FormUrlEncoded
    @POST("/api/v1/authenticate")
    suspend fun authenticate(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<Unit>

    @GET("/api/v3/item/{itemId}") //TODO: add api path
    suspend fun getItem(@Header("Authorization") token:String?, @Path("itemId") itemId: Long): Response<Item>
}
