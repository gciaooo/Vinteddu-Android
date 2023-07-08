package it.unical.gciaoo.vinteddu_android.ApiConfig
import it.unical.gciaoo.vinteddu_android.model.Item
import it.unical.gciaoo.vinteddu_android.model.UtenteDTO
import it.unical.gciaoo.vinteddu_android.model.Wallet
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import java.math.BigDecimal
import java.time.LocalDate

interface ApiService {


    @GET("/api/v2/user/{token}")
    suspend fun getCurrentUser(@Header("Authorization") token:String?, @Path("token") token_: String?) : Response<UtenteDTO>

    @GET("/api/v2/wallet/{token}")
    suspend fun getSaldo(@Header("Authorization") token:String?, @Path("token") token_: String?) : Response<Wallet>

    @FormUrlEncoded
    @POST("/api/v2/WalletCharge/{token}")
    suspend fun wallet_recharge(@Header("Authorization") token:String?,  @Path("token") token_: String?, @Field("amount") amount: Int?) : Response<Unit>

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

    @GET("/api/v3/search/{nome}/{token}")
    suspend fun getSearch(@Header("Authorization") token:String?, @Path("nome") nome: String, @Path("token") token_: String?): Response<List<Item>>

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


    @FormUrlEncoded
    @POST("/api/v1/buyItem/{item}")
    suspend fun buyItem(@Header("Authorization") token:String?,  @Path("item") itemId: Long?, @Field("token") token_: String?) : Response<Unit>
    @GET("/api/v1/SaldoOk/{itemId}/{token}")
    suspend fun saldo(@Header("Authorization") token:String?, @Path("itemId") itemId: Long, @Path("token") token_: String?): Response<Unit>

    @GET("/api/v2/getPurchase/{token}")
    suspend fun getItemAcquistati(@Header("Authorization") token:String?, @Path("token") token_: String?): Response<List<Item>>


    @FormUrlEncoded
    @POST("/api/v1/addFavorites/{item}")
    suspend fun addFavorites(@Header("Authorization") token:String?,  @Path("item") itemId: Long?, @Field("token") token_: String?) : Response<String>


}
