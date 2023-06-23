package it.unical.gciaoo.vinteddu_android.model

import java.time.LocalDate

data class UtenteDTO(
    var id: Long,
    var username: String,
    var nome: String,
    var cognome: String,
    var email: String,
    var password: Any? = null,
    var dataNascita: List<Long>,
    var indirizzo: String,
    var numeroTelefono: String
){
    companion object {
        fun validateUsername(username: String): Boolean {
            if (username.length in 1..20) {
                return true
            }
            return false
        }

        fun validateFirstName(firstName: String): Boolean {
            if (firstName.length in 1..50)
                return true
            return false
        }

        fun validateLastName(lastName: String): Boolean {
            if (lastName.length in 1..50)
                return true
            return false
        }

        fun validateEmail(email: String): Boolean {
            if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                return true
            return false
        }

        fun validatePhoneNumber(phoneNumber: String): Boolean {
            if (phoneNumber.isNotEmpty() && android.util.Patterns.PHONE.matcher(phoneNumber)
                    .matches()
            )
                return true
            return false
        }

        fun validateBirthDate(birthDate: LocalDate): Boolean {
            if (birthDate.minusYears(LocalDate.now().year.toLong()).year < 13)
                return true
            return false
        }
    }
}

