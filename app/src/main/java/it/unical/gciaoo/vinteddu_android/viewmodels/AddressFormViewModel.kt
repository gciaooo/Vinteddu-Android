package it.unical.gciaoo.vinteddu_android.viewmodels

import it.unical.gciaoo.vinteddu_android.model.Address
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AddressState(
    val street: String = "",
    val streetNumber: String = "",
    val city: String = "",
    val province: String = "",
    val zipCode: String = "",
    val country: String = "",

    val isStreetError: Boolean = !Address.validateStreet(street = street),
    val isStreetNumberError: Boolean = !Address.validateStreetNumber(streetNumber = streetNumber),
    val isCityError: Boolean = !Address.validateCity(city = city),
    val isProvinceError: Boolean = !Address.validateProvince(province = province),
    val isZipCodeError: Boolean = !Address.validateZipCode(zipCode = zipCode),
    val isCountryError: Boolean = !Address.validateCountry(country = country)
)

class AddressFormViewModel {
    private val _addressState = MutableStateFlow(AddressState())
    val addressState : StateFlow<AddressState> = _addressState.asStateFlow()

    fun updateStreet(street: String) {
        val hasError = !Address.validateStreet(street = street)
        _addressState.value = _addressState.value.copy(
            street = street,
            isStreetError = hasError
        )
    }

    fun updateStreetNumber(streetNumber: String) {
        val hasError = !Address.validateStreetNumber(streetNumber = streetNumber)
        _addressState.value = _addressState.value.copy(
            streetNumber = streetNumber,
            isStreetNumberError = hasError
        )
    }

    fun updateCity(city: String) {
        val hasError = !Address.validateCity(city = city)
        _addressState.value = _addressState.value.copy(
            city = city,
            isCityError = hasError
        )
    }

    fun updateProvince(province: String) {
        val hasError = !Address.validateProvince(province = province)
        _addressState.value = _addressState.value.copy(
            province = province,
            isProvinceError = hasError
        )
    }

    fun updateZipCode(zipCode: String) {
        val hasError = !Address.validateZipCode(zipCode = zipCode)
        _addressState.value = _addressState.value.copy(
            zipCode = zipCode,
            isZipCodeError = hasError
        )
    }

    fun updateCountry(country: String) {
        val hasError = !Address.validateCountry(country = country)
        _addressState.value = _addressState.value.copy(
            country = country,
            isCountryError = hasError
        )
    }
}
