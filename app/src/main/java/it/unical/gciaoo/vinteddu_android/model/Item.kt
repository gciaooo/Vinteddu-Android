package it.unical.gciaoo.vinteddu_android.model

import android.graphics.Bitmap
import java.math.BigDecimal


enum class Status {
    ON_SALE,
    IN_DELIVERY,
    DELIVERED,
    ABORTED
}

data class Item(
//    val id: Long?,
//    val name: String,
//    val description: String,
//    val price: BigDecimal,
//    val creationDate: List<Int>,
//    val status: String,
//    //val seller : UtenteDTO,
//    val images: /*List<Bitmap>*/ String

    val id: Long?,
    var nome: String,
    var descrizione: String?,
    var prezzo: Long?,
    val dataCreazione: List<Int>?,
    val stato: String,
    val idUtente: Long?,
    var immagini: String?
)