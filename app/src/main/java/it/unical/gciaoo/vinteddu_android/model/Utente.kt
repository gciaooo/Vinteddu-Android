package it.unical.gciaoo.vinteddu_android.model

import java.time.LocalDate

data class Utente(
    var id: Long? ,
    var username: String?,
    var nome: String? ,
    var cognome: String? ,
    var email: String? ,
    var password: String? ,
    var dataNascita: LocalDate? ,
    var indirizzo: String?,
    var numeroTelefono: String?
)