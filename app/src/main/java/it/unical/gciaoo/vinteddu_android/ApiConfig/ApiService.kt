package it.unical.gciaoo.vinteddu_android.ApiConfig
import it.unical.gciaoo.vinteddu_android.model.Item
import it.unical.gciaoo.vinteddu_android.model.User
import it.unical.gciaoo.vinteddu_android.model.Utente
import it.unical.gciaoo.vinteddu_android.model.UtenteDTO
import it.unical.gciaoo.vinteddu_android.model.Wallet
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.TemporalAmount

interface ApiService {


    @GET("/api/v2/user/{token}")
    suspend fun getCurrentUser(@Header("Authorization") token:String?, @Path("token") token_: String?) : Response<UtenteDTO>

    @GET("/api/v2/wallet/{userId}")
    suspend fun getSaldo(@Header("Authorization") token:String?, @Path("userId") id: Long?) : Response<Wallet>

    @FormUrlEncoded
    @POST("/api/v2/Wallet/{userId}")
    suspend fun wallet_recharge(@Header("Authorization") token:String?,  @Path("userId") id: Long?, @Field("amount") amount: Int?) : Response<String>

    @FormUrlEncoded
    @POST("/api/v1/authenticate")
    suspend fun authenticate(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<Unit>

    @FormUrlEncoded
    @POST("/api/v1/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("nome") nome: String,
        @Field("cognome") cognome: String,
        @Field("datanascita") datanascita: LocalDate,
        @Field("indirizzo") indirizzo: String,
        @Field("numerotelefono") numerotelefono: String,
    ): Response<String>

    @GET("/api/v3/item/{itemId}")
    suspend fun getItem(@Header("Authorization") token:String?, @Path("itemId") itemId: Long): Response<Item>

    @GET("/api/v3/search/{nome}")
    suspend fun getSearch(@Header("Authorization") token:String?, @Path("nome") nome: String): Response<List<Item>>

    @GET("/api/v2/Favorites/{token}")
    suspend fun getFavorites(@Header("Authorization") token:String?, @Path("token") token_: String): Response<List<Item>>

    @GET("/api/v2/inVendita/{token}")
    suspend fun getItemInVendita(@Header("Authorization") token:String?, @Path("token")token_: String): Response<List<Item>>


    @FormUrlEncoded
    @POST("/api/v3/add/{token}")
    suspend fun addItem(
        @Header("Authorization") token:String?,
        @Path("token") token_: String?,
        @Field("nome") nome: String,
        @Field("descrizione") descrizione: String,
        @Field("prezzo") prezzo: BigDecimal,
        @Field("immagine") immagine : String
    ): Response<String>

    @FormUrlEncoded
    @POST("/api/v1/newPassword")
    suspend fun getNewPassword(
        @Field("email") email: String,
        @Field("username") username: String,
    ): Response<String>

}
