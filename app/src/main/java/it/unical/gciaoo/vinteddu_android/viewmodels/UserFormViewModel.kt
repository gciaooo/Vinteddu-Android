package it.unical.gciaoo.vinteddu_android.viewmodels

import it.unical.gciaoo.vinteddu_android.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

data class UserState(
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val birthDate: LocalDate = LocalDate.now(),
    val phoneNumber: String = "",

    val passwordConfirm: String = "",

    val isUsernameError: Boolean = !User.validateUsername(username = username),
    val isFirstNameError: Boolean = !User.validateFirstName(firstName = firstName),
    val isLastNameError: Boolean = !User.validateLastName(lastName = lastName),
    val isEmailError: Boolean = !User.validateEmail(email = email),
    val isPasswordError: Boolean = !User.validatePassword(password = password),
    val isBirthDateError: Boolean = !User.validateBirthDate(birthDate = birthDate),
    val isPhoneNumberError: Boolean = !User.validatePhoneNumber(phoneNumber = phoneNumber),

    val isPasswordConfirmError: Boolean = passwordConfirm.isEmpty()
)

class UserFormViewModel {
    private val _userState = MutableStateFlow(UserState())
    val userState : StateFlow<UserState> = _userState.asStateFlow()

    fun updateUsername(username: String) {
        val hasError = !User.validateUsername(username = username)
        _userState.value = _userState.value.copy(
            username = username,
            isUsernameError = hasError
        )
    }

    fun updateFirstName(firstName: String) {
        val hasError = !User.validateFirstName(firstName = firstName)
        _userState.value = _userState.value.copy(
            firstName = firstName,
            isFirstNameError = hasError
        )
    }

    fun updateLastName(lastName : String) {
        val hasError = !User.validateLastName(lastName = lastName)
        _userState.value = _userState.value.copy(
            lastName = lastName,
            isLastNameError = hasError
        )
    }

    fun updateEmail(email: String) {
        val hasError = !User.validateEmail(email = email)
        _userState.value = _userState.value.copy(
            email = email,
            isEmailError = hasError
        )
    }

    fun updatePassword(password: String) {
        val hasError = !User.validatePassword(password = password)
        _userState.value = _userState.value.copy(
            password = password,
            isEmailError = hasError
        )
    }

    fun updatePasswordConfirm(passwordConfirm : String, password : String) {
        val hasError = passwordConfirm.isEmpty() && password != passwordConfirm
        _userState.value = _userState.value.copy(
            passwordConfirm = passwordConfirm,
            isPasswordConfirmError = hasError
        )
    }

    fun updateBirthDate(birthDate: LocalDate) {
        val hasError = !User.validateBirthDate(birthDate)
        _userState.value = _userState.value.copy(
            birthDate = birthDate,
            isBirthDateError = hasError
        )
    }

    fun updatePhoneNumber(phoneNumber: String) {
        val hasError = !User.validatePhoneNumber(phoneNumber)
        _userState.value = _userState.value.copy(
            phoneNumber = phoneNumber,
            isPhoneNumberError = hasError
        )
    }
}