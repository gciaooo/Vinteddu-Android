package it.unical.gciaoo.vinteddu_android

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unical.gciaoo.vinteddu_android.ApiConfig.ApiService
import it.unical.gciaoo.vinteddu_android.ApiConfig.SessionManager
import it.unical.gciaoo.vinteddu_android.model.Item
import it.unical.gciaoo.vinteddu_android.model.UtenteDTO
import it.unical.gciaoo.vinteddu_android.model.Wallet
import it.unical.gciaoo.vinteddu_android.ui.theme.Typography
import it.unical.gciaoo.vinteddu_android.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
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
    val commonModifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
    val coroutineScope = rememberCoroutineScope()
    var intFieldValue by remember { mutableStateOf("") }
    LaunchedEffect(key1 = token, key2 = userDto.value) {
        // Effettua la chiamata API per ottenere UtenteDTO
        val response = apiService.getCurrentUser("Bearer $token", token)
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
                text = "Saldo:$ ${wallet.value?.saldo}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp)
            ) {
                Button(
                    content={
                            Text("Ricarica wallet")
                    },
                    modifier = commonModifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp)
                        .height(IntrinsicSize.Max),
                    onClick = {
                        coroutineScope.launch {
                            try {
                                val intValue = intFieldValue.toIntOrNull()
                                if (intValue != null) {
                                    val resp = apiService.wallet_recharge("Bearer $token",userDto.value?.id,intValue)
                                } else {
                                    // Valore inserito non è un intero valido
                                }
                            } catch (e: Exception) {
                                // Si è verificato un errore durante la chiamata API
                            }
                        }
                    },
                )


                TextField(
                    value = intFieldValue,
                    onValueChange = { value ->
                        intFieldValue = value.takeIf { it.isEmpty() || it.toIntOrNull() != null } ?: intFieldValue
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text("Inserisci l'importo della ricarica") },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                )
            }
//clic
        }

    }else{
        sleep(2000)
    }
}

@Composable
fun PaginaPreferiti(apiService: ApiService, sessionManager: SessionManager, navHostController:NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val token = sessionManager.getToken()
    val preferiti = remember { mutableListOf<Item>() }

    LaunchedEffect(key1 = token, key2 = preferiti){
        val response = apiService.getFavorites("Bearer $token", token!!)
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
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
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
                                        navHostController.navigate("items/${prodotto.id}")
                                    }


                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Prezzo: ${formattaPrezzo(prodotto.prezzo!!)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }else{
        sleep(3000)
    }
}

@Composable
fun PaginaProdottiInVendita(apiService: ApiService, sessionManager: SessionManager, navHostController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val token = sessionManager.getToken()
    val preferiti = remember { mutableListOf<Item>() }
    LaunchedEffect(key1 = token, key2 = preferiti){
        val response = apiService.getItemInVendita("Bearer $token", token!!)
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
                text = "Oggetti in vendita o venduti",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
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
                                        navHostController.navigate("items/${prodotto.id}")
                                    }


                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Prezzo: ${formattaPrezzo(prodotto.prezzo!!)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Stato: ${(prodotto.stato)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }else{
        sleep(3000)
    }
}

@Composable
fun AddItem(navHostController: NavHostController, apiService: ApiService, sessionManager: SessionManager) {
    val commonModifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)

    val token = sessionManager.getToken()
//    val item by remember {
//        mutableStateOf(Item(null,"","",null,null,"",null,""))
//    }
//    val item = remember { mutableStateOf<Item?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val nameState = remember { mutableStateOf("") }
    val descriptionState = remember { mutableStateOf("") }
    val priceState = remember { mutableStateOf("") }
    var imagePath by remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // Ottieni il percorso dell'immagine dalla URI
        val imageFile = File(uri!!.path)
        if (imageFile.exists()) {
            // Copia l'immagine nella directory delle immagini dell'app
            val outputDir = context.filesDir
            val outputFile = File(outputDir, imageFile.name)
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(outputFile)
            inputStream?.copyTo(outputStream)
            imagePath = outputFile.absolutePath
        }
    }
    Column(
        modifier = Modifier.padding(all = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.1f))
//        Text(
//            text = stringResource(R.string.login), style = Typography.headlineLarge,
//            modifier = Modifier
//                .align(Alignment.Start)
//                .padding(horizontal = 20.dp)
//                .weight(0.3f)
//        )
        Spacer(modifier = Modifier.weight(0.2f))
        InputField(name = "Titolo inserzione", commonModifier, nameState)
        InputField(name = "Descrizione", commonModifier, descriptionState )
        InputField(name = "Prezzo", commonModifier, priceState )
        if (imagePath.isNotEmpty()) {
            val imagePainter: Painter = painterResource(R.drawable.felpa)
            Image(
                painter = imagePainter,
                contentDescription = "Immagine selezionata",
                modifier = Modifier
                    .size(200.dp)
                    .padding(16.dp),
                contentScale = ContentScale.Crop
            )
        }

        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Carica immagine")
        }


        Button(content = {
            Text("Metti in vendita")

        },
            modifier = commonModifier
                .padding(vertical = 30.dp)
                .height(IntrinsicSize.Max),
            onClick = {
                val nome = nameState.value
                val descrizione = descriptionState.value
                val price = priceState.value.toBigDecimal()
                coroutineScope.launch {
                    try {
                        showDialog.value=true
                        val response = apiService.addItem("Bearer $token",token, nome, descrizione, price, imagePath)
                    } catch (e: Exception) {
                        // Si è verificato un errore durante la chiamata API
                    }
                }
            }
        )
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    showDialog.value = false
                },
                title = {
                    Text(text = "Oggetto ufficialmente messo in vendita")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            navHostController.navigate(Routes.HOME.route)
                            showDialog.value = false // Chiudi il popup
                        }
                    ) {
                        Text(text = "OK")
                    }
                },
                modifier = Modifier.padding(16.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Back Home",
            modifier = Modifier
                .padding(vertical = 15.dp)
                .clickable(onClick = { navHostController.navigate(Routes.HOME.route) })
        )
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

