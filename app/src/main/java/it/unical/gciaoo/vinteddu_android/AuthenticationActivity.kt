package it.unical.gciaoo.vinteddu_android

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unical.gciaoo.vinteddu_android.ApiConfig.ApiService
import it.unical.gciaoo.vinteddu_android.ApiConfig.SessionManager
import it.unical.gciaoo.vinteddu_android.ui.theme.Typography
import it.unical.gciaoo.vinteddu_android.viewmodels.AddressFormViewModel
import it.unical.gciaoo.vinteddu_android.viewmodels.UserFormViewModel
import kotlinx.coroutines.launch

@Composable
fun Login(navHostController: NavHostController, apiService: ApiService) {
    val commonModifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)


    val coroutineScope = rememberCoroutineScope()
    val usernameState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.padding(all = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.1f))
        Text(
            text = stringResource(R.string.login), style = Typography.headlineLarge,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 20.dp)
                .weight(0.3f)
        )
        Spacer(modifier = Modifier.weight(0.2f))
        InputField(name = stringResource(R.string.login_username), commonModifier, usernameState)
        InputField(name = stringResource(R.string.login_password), commonModifier, passwordState)
        Button(content = {
            Text(stringResource(R.string.login))

        },
            modifier = commonModifier
                .padding(vertical = 30.dp)
                .height(IntrinsicSize.Max),
            onClick = {
                val username = usernameState.value
                val password = passwordState.value

                coroutineScope.launch {
                    try {
                        val response = apiService.authenticate(username, password)
                        if(response.isSuccessful){
                            navHostController.navigate(Routes.PROFILE.route)
                            //navHostController.navigate(ItemPage(apiService = apiService, sessionManager = SessionManager()))
                        }

                    } catch (e: Exception) {
                        // Si è verificato un errore durante la chiamata API
                    }
                }
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            stringResource(R.string.register_prompt),
            modifier = Modifier
                .padding(vertical = 15.dp)
                .clickable(onClick = { navHostController.navigate("login/new") })
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(userFormViewModel: UserFormViewModel, addressFormViewModel: AddressFormViewModel, navHostController: NavHostController, apiService: ApiService) {
    val coroutineScope = rememberCoroutineScope()
    val userState by userFormViewModel.userState.collectAsState()
    val nameEmailError by remember { derivedStateOf { userState.isUsernameError || userState.isEmailError } }
    val addressState by addressFormViewModel.addressState.collectAsState()
    val shippingError by remember { derivedStateOf { userState.isFirstNameError || userState.isLastNameError || addressState.isStreetError || addressState.isStreetNumberError || addressState.isCityError || addressState.isProvinceError || addressState.isZipCodeError || userState.isPhoneNumberError } }
    val passwordError by remember { derivedStateOf { userState.isPasswordError || userState.isPasswordConfirmError } }
    val commonModifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.weight(0.1f))
        Text(
            text = stringResource(R.string.register),
            style = Typography.headlineLarge,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        )
        Spacer(modifier = Modifier.weight(0.3f))
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = userState.username,
                    onValueChange = { userFormViewModel.updateUsername(it) },
                    label = { Text(stringResource(R.string.user_username)) },
                    singleLine = true,
                    isError = userState.isUsernameError,
                    modifier = commonModifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = userState.email,
                    onValueChange = { userFormViewModel.updateEmail(it) },
                    label = { Text(stringResource(R.string.user_email)) },
                    singleLine = true,
                    isError = userState.isEmailError,
                    modifier = commonModifier.fillMaxWidth()
                )
                if (nameEmailError) {
                    Text(
                        stringResource(R.string.user_nameemailerror),
                        style = Typography.bodyMedium,
                        modifier = commonModifier
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 20.dp))
                Row() {
                    OutlinedTextField(
                        value = userState.firstName,
                        onValueChange = { userFormViewModel.updateFirstName(it) },
                        label = { Text(stringResource(R.string.user_firstname)) },
                        isError = userState.isFirstNameError,
                        singleLine = true,
                        modifier = commonModifier.weight(0.5f)
                    )
                    OutlinedTextField(
                        value = userState.lastName,
                        onValueChange = { userFormViewModel.updateLastName(it) },
                        label = { Text(stringResource(R.string.user_lastName)) },
                        isError = userState.isLastNameError,
                        singleLine = true,
                        modifier = commonModifier.weight(0.5f)
                    )
                }
                //TODO: add birthDate form
                Row() {
                    OutlinedTextField(
                        value = addressState.street,
                        onValueChange = { addressFormViewModel.updateStreet(it) },
                        label = { Text(stringResource(R.string.user_street)) },
                        isError = addressState.isStreetError,
                        singleLine = true,
                        modifier = commonModifier.weight(0.70f)
                    )
                    OutlinedTextField(
                        value = addressState.streetNumber,
                        onValueChange = { addressFormViewModel.updateStreetNumber(it) },
                        label = { Text(stringResource(R.string.user_streetnumber)) },
                        isError = addressState.isStreetNumberError,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = commonModifier.weight(0.30f),
                    )
                }
                Row() {
                    OutlinedTextField(
                        value = addressState.city,
                        onValueChange = { addressFormViewModel.updateCity(it) },
                        label = { Text(stringResource(R.string.user_city)) },
                        isError = addressState.isCityError,
                        singleLine = true,
                        modifier = commonModifier.weight(0.33f)
                    )
                    OutlinedTextField(
                        value = addressState.province,
                        onValueChange = { addressFormViewModel.updateProvince(it) },
                        label = { Text(stringResource(R.string.user_province)) },
                        isError = addressState.isProvinceError,
                        singleLine = true,
                        modifier = commonModifier.weight(0.33f)
                    )
                    OutlinedTextField(
                        value = addressState.zipCode,
                        onValueChange = { addressFormViewModel.updateZipCode(it) },
                        label = { Text(stringResource(R.string.user_zipcode)) },
                        isError = addressState.isZipCodeError,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = commonModifier.weight(0.33f)
                    )
                    //TODO: add country field
                }
                OutlinedTextField(
                    value = userState.phoneNumber,
                    onValueChange = { userFormViewModel.updatePhoneNumber(it) },
                    label = { Text(stringResource(R.string.user_phonenumber)) },
                    isError = userState.isPhoneNumberError,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                    modifier = commonModifier.fillMaxWidth()
                )
                if (shippingError) {
                    Text(
                        stringResource(R.string.user_shippingerror),
                        style = Typography.bodyMedium,
                        modifier = commonModifier
                    )
                }
                Spacer(modifier = Modifier.padding(20.dp))
                OutlinedTextField(
                    value = userState.password,
                    onValueChange = {
                        userFormViewModel.updatePassword(it)
                    },
                    label = { Text(stringResource(R.string.user_password)) },
                    isError = userState.isPasswordError,
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    modifier = commonModifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = userState.passwordConfirm,
                    onValueChange = {
                        userFormViewModel.updatePasswordConfirm(
                            it,
                            userState.password
                        )
                    },
                    label = { Text(stringResource(R.string.user_password_confirm)) },
                    isError = userState.isPasswordConfirmError,
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    modifier = commonModifier.fillMaxWidth()
                )
                if (passwordError) {
                    Text(
                        stringResource(R.string.user_passworderror),
                        style = Typography.bodyMedium,
                        modifier = commonModifier
                    )
                }
                Button(content = {
                    Text(stringResource(R.string.register))

                },
                    modifier = commonModifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp)
                        .height(IntrinsicSize.Max),
                    onClick = {
                        val username = userState.username
                        val password = userState.password
                        val email = userState.email
                        val nome = userState.lastName
                        val cognome = userState.firstName
                        val telefono = userState.phoneNumber
                        val dataNascita = userState.birthDate
                        val indirizzo = addressState.street+" "+addressState.city+" "+addressState.province+" "+addressState.country

                        coroutineScope.launch {
                            try {
                                val response = apiService.register(username, password, email, nome, cognome, dataNascita, indirizzo, telefono )
                                if(response.isSuccessful){
                                    navHostController.navigate(Routes.LOGIN.route)
                                    //navHostController.navigate(ItemPage(apiService = apiService, sessionManager = SessionManager()))
                                }

                            } catch (e: Exception) {
                                // Si è verificato un errore durante la chiamata API
                            }
                        }


                    }
                )
            }
        }

    }
}

@Composable
fun InputField(name: String, modifier: Modifier, usernameState: MutableState<String>) {
    //var field by remember { mutableStateOf("") }
    Row(horizontalArrangement = Arrangement.Center) {
        TextField(
            value = usernameState.value,
            onValueChange = { newValue ->
                usernameState.value = newValue },
            label = { Text(name) },
            singleLine = true,
            modifier = modifier.padding(vertical = 5.dp)
        )
    }
}