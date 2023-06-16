package it.unical.gciaoo.vinteddu_android.model

import android.graphics.Bitmap
import java.math.BigDecimal
import java.time.LocalDate


enum class Status {
    ON_SALE,
    IN_DELIVERY,
    DELIVERED,
    ABORTED
}

class Item(
    val id: Long,
    val name: String,
    val description: String = "",
    val price: BigDecimal,
    val creationDate: LocalDate,
    val status: Status,
    val images: List<Bitmap>
)