package it.unical.gciaoo.vinteddu_android

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unical.gciaoo.vinteddu_android.ApiConfig.ApiService
import it.unical.gciaoo.vinteddu_android.ApiConfig.SessionManager
import it.unical.gciaoo.vinteddu_android.model.Item
import it.unical.gciaoo.vinteddu_android.model.UtenteDTO
import it.unical.gciaoo.vinteddu_android.model.Wallet
import it.unical.gciaoo.vinteddu_android.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.lang.Thread.sleep
import java.text.NumberFormat



@Composable
fun Profile(apiService: ApiService, userViewModel: UserViewModel, sessionManager: SessionManager
) {

    val userState by userViewModel.userState.collectAsState()
    val errorMessageState = remember { mutableStateOf("") }
    val token = sessionManager.getToken();
    val userDto = remember { mutableStateOf<UtenteDTO?>(null) }
    LaunchedEffect(key1 = token, key2 = userDto.value) {
        // Effettua la chiamata API per ottenere UtenteDTO
        val response = apiService.getCurrentUser("Bearer $token", token)
        if (response.isSuccessful) {
            userDto.value = response.body()
            updateProfile(userDto.value, userViewModel)// Aggiorna userState nel ViewModel
        } else {
            errorMessageState.value = "Errore durante la richiesta dell'utente"
        }
    }
    if (userDto.value != null) {
        val userFields = mapOf(
            R.string.user_username to userDto.value?.username,
            R.string.user_email to userDto.value?.email,
            R.string.user_firstName to userDto.value?.nome,
            R.string.user_lastName to userDto.value?.cognome,
            R.string.user_shippingAddress to userDto.value?.indirizzo,
            R.string.user_phonenumber to userDto.value?.numeroTelefono
        )
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
            Text(
                text = stringResource(R.string.profile_page),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(20.dp)
            )
            for (field in userFields.keys) {
                Card(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp), horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(text = stringResource(field),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.weight(.5f)
                            )
                        Text(text = "${userFields[field]}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(.5f)
                            )
                    }
                    Divider()
                }
            }
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


            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(preferiti) { prodotto ->
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
        }
    }else{
        sleep(3000)
    }
}

@Composable
fun PaginaProdottiInVendita(apiService: ApiService, sessionManager: SessionManager, navHostController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val token = sessionManager.getToken()
    val items = remember { mutableListOf<Item>() }
    LaunchedEffect(key1 = token, key2 = items){
        val response = apiService.getItemInVendita("Bearer $token", token!!)
        if(response.isSuccessful){
            for(item in response.body()!!){
                items.add(item)
            }
        }
    }

    if(items.isNotEmpty()) {
        Column(
            modifier = Modifier.padding(16.dp)

        ) {
            Text(
                text = "Oggetti in vendita o venduti",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )


            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(items) { prodotto->
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

        }
    }else{
        sleep(3000)
    }
}

@Composable
fun PaginaProdottiAcquistati(apiService: ApiService, sessionManager: SessionManager, navHostController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val token = sessionManager.getToken()
    val acquisti = remember { mutableListOf<Item>() }
    LaunchedEffect(key1 = token, key2 = acquisti){
        val response = apiService.getItemAcquistati("Bearer $token", token!!)
        if(response.isSuccessful){
            for(item in response.body()!!){
                acquisti.add(item)
            }
        }
    }

    if(acquisti.isNotEmpty()) {
        Column(
            modifier = Modifier.padding(16.dp)

        ) {
            Text(
                text = "Oggetti acquistati",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )


            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(acquisti) { item ->
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
                                Text(
                                    text = item.nome,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Prezzo: ${formattaPrezzo(item.prezzo!!)}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Stato: ${(item.stato)}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
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
        InputField(name = stringResource(R.string.item_title), commonModifier, nameState)
        InputField(name = stringResource(R.string.item_description) , commonModifier, descriptionState )
        InputField(name = stringResource(R.string.item_price) , commonModifier, priceState )
//        if (imagePath.isNotEmpty()) {
//            val imagePainter: Painter = painterResource(R.drawable.felpa)
//            Image(
//                painter = imagePainter,
//                contentDescription = "Immagine selezionata",
//                modifier = Modifier
//                    .size(200.dp)
//                    .padding(16.dp),
//                contentScale = ContentScale.Crop
//            )
//        }

        Button(
            modifier = commonModifier
                .padding(vertical = 30.dp)
                .height(IntrinsicSize.Max),
            onClick = { launcher.launch("image/*") }
        ) {
            Text(stringResource(R.string.add_item_image))
        }


        Button(content = {
            Text(stringResource(R.string.add_item_sale))

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
                    Text(text = stringResource(R.string.add_item_ok) )
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

@Composable
fun Wallet(apiService: ApiService, sessionManager: SessionManager, navHostController: NavHostController) {
    val wallet = remember { mutableStateOf<Wallet?>(null) }
    val token = sessionManager.getToken()
    val commonModifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
    val coroutineScope = rememberCoroutineScope()
    var intFieldValue by remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    val showDialog2 = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = token, key2 = wallet){
        val response = apiService.getSaldo("Bearer $token", token!!)
        if(response.isSuccessful){
            wallet.value = response.body();
        }
    }

    if(wallet.value!=null) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
        {


            Text(
                text = stringResource(R.string.balance),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 30.dp)
            )
            Text(
                text = wallet.value?.saldo.toString(),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )


            Card(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        if (showDialog.value) {
                            AlertDialog(
                                onDismissRequest = {
                                    showDialog.value = false
                                },
                                title = {
                                    Text(text = stringResource(R.string.wallet_error))
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            showDialog.value = false // Chiudi il popup
                                        }
                                    ) {
                                        Text(text = "OK")
                                    }
                                },
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        if (showDialog2.value) {
                            AlertDialog(
                                onDismissRequest = {
                                    showDialog2.value = false
                                },
                                title = {
                                    Text(text = stringResource(R.string.balance_recharge_ok))
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            navHostController.navigate(Routes.WALLET.route)
                                            showDialog2.value = false // Chiudi il popup
                                        }
                                    ) {
                                        Text(text = "OK")
                                    }
                                },
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        TextField(
                            value = intFieldValue,
                            onValueChange = { value ->
                                intFieldValue = value.takeIf { it.isEmpty() || it.toIntOrNull() != null } ?: intFieldValue
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            label = { Text(stringResource(R.string.balance_recharge)) },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(bottom = 16.dp)
                                .fillMaxWidth()
                        )
                        Button(
                            content = {
                                Text(stringResource(R.string.balance_recharge_button_prompt))
                            },
                            modifier = commonModifier
                                .fillMaxWidth()
                                .padding(vertical = 30.dp)
                                .height(IntrinsicSize.Max),
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        val intValue = intFieldValue.toIntOrNull()
                                        if (intValue != null && intValue > 0) {
                                            val resp = apiService.wallet_recharge(
                                                "Bearer $token",
                                                token,
                                                intValue
                                            )
                                            if(resp.isSuccessful){
                                                showDialog2.value=true
                                            }
                                        } else {
                                            showDialog.value=true
                                        }
                                    } catch (e: Exception) {
                                        // Si è verificato un errore durante la chiamata API
                                    }
                                }
                            },
                        )
                    }
                }
            }
        }
    }else{
        sleep(3000)
    }
}

fun formattaPrezzo(prezzo: Long): String {
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

