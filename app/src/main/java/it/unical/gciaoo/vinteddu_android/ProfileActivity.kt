package it.unical.gciaoo.vinteddu_android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unical.gciaoo.vinteddu_android.viewmodels.UserFormViewModel

@Composable
fun Profile(
    userFormViewModel: UserFormViewModel = UserFormViewModel(),
    apiService: ApiService,
    sessionManager: SessionManager
) {
    val userState by userFormViewModel.userState.collectAsState()
    val errorMessageState = remember { mutableStateOf("") }
    //val user_State = remember { mutableStateOf<String?>(null) }

    val token = sessionManager.getToken();
    //val username = sessionManager.getUsername(token);

    //val username = getUsernameFromToken(token);
    LaunchedEffect(Unit) {
        try {
            val response = apiService.getCurrentUser("Bearer $token", token)
            if (response.isSuccessful) {
                val account = response.body();
                account?.let {
                    userFormViewModel.updateUsername(account.username)
//                    userFormViewModel.updateFirstName(account.firstName)
//                    userFormViewModel.updateLastName(account.lastName)
//                    userFormViewModel.updateEmail(account.email)
//                    userFormViewModel.updateBirthDate(account.birthDate)
//                    userFormViewModel.updatePhoneNumber(account.phoneNumber)
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = "Errore durante la chiamata API: $errorBody"
                errorMessageState.value = errorMessage
            }
        } catch (e: Exception) {
            val errorMessage = "Si è verificato un errore durante la chiamata API"
            errorMessageState.value = errorMessage
        }
    }



    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Text(
            text = "Username: ${userState.username}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = "Email: ${userState.email}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = "Nome: ${userState.firstName}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = "Cognome: ${userState.lastName}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}