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
import it.unical.gciaoo.vinteddu_android.ApiConfig.ApiService
import it.unical.gciaoo.vinteddu_android.ApiConfig.SessionManager
import it.unical.gciaoo.vinteddu_android.model.Utente
import it.unical.gciaoo.vinteddu_android.model.UtenteDTO
import it.unical.gciaoo.vinteddu_android.viewmodels.UserViewModel
import retrofit2.Response

@Composable
fun Profile(apiService: ApiService, userViewModel: UserViewModel, sessionManager: SessionManager
) {

    val userState by userViewModel.userState.collectAsState()
    val errorMessageState = remember { mutableStateOf("") }
    val token = sessionManager.getToken();
    val userDto = remember { mutableStateOf<UtenteDTO?>(null) }
    LaunchedEffect(key1 = token) {
        // Effettua la chiamata API per ottenere UtenteDTO

        val response = apiService.getCurrentUser("Bearer $token", token)
        if (response.isSuccessful) {
            userDto.value = response.body()
            updateProfile(userDto.value, userViewModel)// Aggiorna userState nel ViewModel
        } else {
            errorMessageState.value = "Errore durante la richiesta dell'utente"
        }
    }


    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        userDto.value?.let { user ->
            Text(
                text = "Username: ${user.username}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "Email: ${user.email}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "Nome: ${user.nome}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "Cognome: ${user.cognome}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}



// Funzione per ottenere i dati dell'utente corrente
suspend fun getCurrentUser(token: String?, apiService: ApiService): UtenteDTO? {
    val response: Response<UtenteDTO> = apiService.getCurrentUser("Bearer $token", token)

    if (response.isSuccessful) {
        return response.body()
    }

    return null
}


// Funzione per aggiornare l'interfaccia utente con i dati ricevuti
fun updateProfile(utente: UtenteDTO?, userViewModel: UserViewModel) {

    if (utente != null) {
        userViewModel.updatePhoneNumber(utente.numeroTelefono)
        userViewModel.updateEmail(utente.email)
        userViewModel.updateFirstName(utente.cognome)
        userViewModel.updateLastName(utente.nome)
        userViewModel.updateUsername(utente.username)
    }

}

