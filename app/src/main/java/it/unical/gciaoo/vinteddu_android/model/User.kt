package it.unical.gciaoo.vinteddu_android.model

import java.time.LocalDate

class User(
    private val id: Long,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val birthDate: LocalDate,
    private val address: Address,
    val phoneNumber: String,
) {
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

        fun validatePassword(password: String): Boolean {
            if (password.isNotEmpty()) {
                if (password.contains(Regex("[a-z]")) and password.contains(Regex("[A-Z]")) and password.contains(
                        Regex("[0-9]")
                    )
                ) {
                    return true
                }
            }
            return false
        }


    }
}

class Address(
    val street: String,
    val streetNumber: Int,
    val city: String,
    val province: String,
    val zipCode: Int,
    val country: String
) {
    override fun toString(): String {
        return "$street, $streetNumber, $zipCode, $city, $province, $country"
    }

    companion object {
        fun validateStreet(street: String): Boolean {
            if (street.isNotEmpty()) {
                return true
            }
            return false
        }

        fun validateStreetNumber(streetNumber: String): Boolean {
            try {
                if (streetNumber.isNotEmpty() && streetNumber.toInt() != 0) {
                    return true
                }
                return false
            } catch (e: Exception) {
                return false
            }
        }

        fun validateCity(city: String): Boolean {
            if (city.isNotEmpty()) {
                return true
            }
            return false
        }

        fun validateProvince(province: String): Boolean {
            if (province.isNotEmpty() && province.length == 2) {
                return true
            }
            return false
        }

        fun validateZipCode(zipCode: String): Boolean {
            try {
                if (zipCode.isNotEmpty() && zipCode.toInt() != 0) {
                    return true
                }
                return false
            } catch (e: Exception) {
                return false
            }
        }

        fun validateCountry(country: String): Boolean {
            if (country.isNotEmpty()) {
                return true
            }
            return false
        }
    }
}