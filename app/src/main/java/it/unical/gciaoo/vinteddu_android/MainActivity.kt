package it.unical.gciaoo.vinteddu_android

import android.os.Bundle
import android.provider.Telephony.Mms.Addr
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import it.unical.gciaoo.vinteddu_android.model.User
import it.unical.gciaoo.vinteddu_android.ui.theme.VintedduAndroidTheme
import it.unical.gciaoo.vinteddu_android.viewmodels.AddressFormViewModel
import it.unical.gciaoo.vinteddu_android.viewmodels.UserFormViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VintedduAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomePage()
                }
            }
        }
    }
}



@Composable
fun NavigationView(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "login") {
        composable("login") {
            Login(navHostController = navHostController)
        }
        composable("login/new") {
            Register(userFormViewModel = UserFormViewModel(), addressFormViewModel = AddressFormViewModel())
        }
        composable("profile") {
            Profile(userFormViewModel = UserFormViewModel(), apiService = RetrofitClient.create() )
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VintedduTopAppBar(navHostController: NavHostController) {
    val currentBackStackEntry by navHostController.currentBackStackEntryAsState()
    val showBackIcon by remember(currentBackStackEntry) { derivedStateOf { navHostController.previousBackStackEntry != null }}
    TopAppBar(title = {Text(stringResource(R.string.title))},
        navigationIcon = {
            if (showBackIcon) {
                IconButton(onClick = { navHostController.popBackStack() }) {
                    Icon (
                        Icons.Filled.KeyboardArrowLeft,
                        contentDescription = stringResource(R.string.back)
                            )
                }
            }
            else {
                IconButton(onClick = {}) {
                    Icon(Icons.Rounded.Menu, contentDescription = stringResource(R.string.drawer_icon))
                }
            }
        },
        
        actions = {
            IconButton(onClick ={ navHostController.navigate("profile") }) {
                Icon(Icons.Filled.Person, contentDescription = stringResource(R.string.profile))
            }
        }
        
    )
}
@Preview
@Composable
fun HomePage() {
    val navHostController = rememberNavController()

    Scaffold(topBar = { VintedduTopAppBar(navHostController = navHostController) }) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it), contentAlignment = Alignment.Center) {
            NavigationView(navHostController = navHostController)
        }
    }
}



@Composable
fun Profile(userFormViewModel: UserFormViewModel = UserFormViewModel(), apiService: ApiService ) {
    val userState by userFormViewModel.userState.collectAsState()
    val errorMessageState = remember { mutableStateOf("") }
    val userIdState = remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) {
        try {
            val response = apiService.getCurrentUserId()
            if (response.isSuccessful) {
                val userId = response.body()
                userIdState.value = userId
            } else {
                //errore
            }
        } catch (e: Exception) {
            //l'eccezione
        }
    }
    val userId = userIdState.value
    LaunchedEffect(Unit) {
        try {
            val response = apiService.getAccount(userId.toString())
            if (response.isSuccessful) {
                val account = response.body()
                account?.let {
                    userFormViewModel.updateUsername(account.username)
                    userFormViewModel.updateFirstName(account.firstName)
                    userFormViewModel.updateLastName(account.lastName)
                    userFormViewModel.updateEmail(account.email)
                    userFormViewModel.updateBirthDate(account.birthDate)
                    userFormViewModel.updatePhoneNumber(account.phoneNumber)
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