package it.unical.gciaoo.vinteddu_android

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import it.unical.gciaoo.vinteddu_android.ApiConfig.ApiService
import it.unical.gciaoo.vinteddu_android.ApiConfig.SessionManager
import it.unical.gciaoo.vinteddu_android.model.Item
import it.unical.gciaoo.vinteddu_android.model.Utente
import it.unical.gciaoo.vinteddu_android.model.UtenteDTO
import it.unical.gciaoo.vinteddu_android.model.Wallet
import it.unical.gciaoo.vinteddu_android.viewmodels.UserViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File
import java.lang.Thread.sleep
import java.math.BigDecimal
import java.text.NumberFormat

@Composable
fun Profile(apiService: ApiService, userViewModel: UserViewModel, sessionManager: SessionManager
) {

    val userState by userViewModel.userState.collectAsState()
    val errorMessageState = remember { mutableStateOf("") }
    val token = sessionManager.getToken();
    val userDto = remember { mutableStateOf<UtenteDTO?>(null) }
    val wallet = remember { mutableStateOf<Wallet?>(null) }
    LaunchedEffect(key1 = token, key2 = userDto.value) {
        // Effettua la chiamata API per ottenere UtenteDTO
        val response = apiService.getCurrentUser("Bearer $token", token)
        sleep(2)
        if (response.isSuccessful) {
            userDto.value = response.body()
            val response_2 = apiService.getSaldo("Bearer $token", userDto.value?.id)
            if(response_2.isSuccessful) {
                wallet.value = response_2.body()
            }
            updateProfile(userDto.value, userViewModel)// Aggiorna userState nel ViewModel
        } else {
            errorMessageState.value = "Errore durante la richiesta dell'utente"
        }
    }

    if (userDto.value != null) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {

            Text(
            text = "Username: ${userDto.value?.username}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "Email: ${userDto.value?.email}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "Nome: ${userDto.value?.nome}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "Cognome: ${userDto.value?.cognome}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "Saldo: ${wallet.value?.saldo}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
//click
        }

    }
}

@Composable
fun PaginaPreferiti(apiService: ApiService, sessionManager: SessionManager) {
    val coroutineScope = rememberCoroutineScope()
    val token = sessionManager.getToken()
    val preferiti = remember { mutableListOf<Item>() }

    LaunchedEffect(key1 = token, key2 = preferiti){
        val response = apiService.getFavorites("Bearer $token", 2)
        if(response.isSuccessful){
            for(item in response.body()!!){
                preferiti.add(item)
            }
        }
    }

    if(preferiti.isNotEmpty()) {
        Column(
            modifier = Modifier.padding(16.dp)

        ) {
            Text(
                text = "Preferiti",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )


            for (prodotto in preferiti) {
                Card(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp)
                    ) {
//                        Box(modifier = Modifier.height(100.dp)) {
//
//                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            ClickableText(
                                text = AnnotatedString(prodotto.nome),
                                onClick = {
                                    coroutineScope.launch {
                                        try{
                                            val cambio_page= apiService.getItem(token,
                                                prodotto.id!!
                                            )

                                            if(cambio_page.isSuccessful){
         //                                       navHostController.navigate()
                                            }
                                        }catch (e: Exception){

                                        }
                                    }


                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Prezzo: ${formattaPrezzo(prodotto.prezzo)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }else{
        sleep(2000)
    }
}


fun formattaPrezzo(prezzo: BigDecimal): String {
    val formatter = NumberFormat.getCurrencyInstance()
    return formatter.format(prezzo)
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

