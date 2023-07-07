package it.unical.gciaoo.vinteddu_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import it.unical.gciaoo.vinteddu_android.ApiConfig.ApiService
import it.unical.gciaoo.vinteddu_android.ApiConfig.RetrofitClient
import it.unical.gciaoo.vinteddu_android.ApiConfig.SessionManager
import it.unical.gciaoo.vinteddu_android.ui.theme.VintedduAndroidTheme
import it.unical.gciaoo.vinteddu_android.viewmodels.AddressFormViewModel
import it.unical.gciaoo.vinteddu_android.viewmodels.UserFormViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import it.unical.gciaoo.vinteddu_android.viewmodels.UserViewModel


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
fun NavigationView(apiService: ApiService, sessionManager: SessionManager, navHostController: NavHostController, isSearchBar: MutableState<Boolean>) {
    NavHost(navController = navHostController, startDestination = Routes.HOME.route) {
        composable(Routes.HOME.route) {
            isSearchBar.value = true
            //TODO: Main page
            MainPage(apiService = apiService, sessionManager = sessionManager)
        }
        composable(Routes.LOGIN.route) {
            isSearchBar.value = false
            Login(
                navHostController = navHostController,
                apiService = apiService,
                sessionManager = sessionManager
            )
        }
        composable(Routes.REGISTER.route) {
            isSearchBar.value = false
            Register(
                userFormViewModel = UserFormViewModel(),
                addressFormViewModel = AddressFormViewModel(),
                apiService = apiService,
                navHostController = navHostController
            )
        }
        composable(Routes.LOSTPASSWORD.route) {
            isSearchBar.value = false
            PasswordLostPage(navHostController= navHostController, apiService= apiService, sessionManager= sessionManager)
        }
        composable(Routes.ITEMS.route, arguments = listOf(navArgument("id") { type = NavType.StringType})) {
            isSearchBar.value = true
            it.arguments?.getString("id")?.let { id ->
                ItemPage(apiService = apiService, sessionManager = sessionManager, itemId = id.toLong(), navHostController=navHostController)
            }
        }
        composable(Routes.PROFILE.route) {
            isSearchBar.value = true
            val userViewModel = UserViewModel()
            //PaginaPreferiti(apiService = apiService, sessionManager = sessionManager, navHostController = navHostController)

            Profile(apiService = RetrofitClient.create(sessionManager), userViewModel, sessionManager = sessionManager)
        }
        composable(Routes.FAVORITES.route) {
            isSearchBar.value = true
            PaginaPreferiti(
                apiService = apiService,
                sessionManager = sessionManager,
                navHostController = navHostController
            )
        }
        composable(Routes.SEARCH.route) { Box(Modifier.fillMaxSize()) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VintedduTopAppBar(
    navHostController: NavHostController,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope
) {
    TopAppBar(title = { Text(stringResource(R.string.title)) },
        navigationIcon = {
            IconButton(onClick = {
                coroutineScope.launch {
                    drawerState.open()
                }
            }) {
                Icon(
                    Icons.Rounded.Menu,
                    contentDescription = stringResource(R.string.drawer_icon)
                )
            }

        },
    )
}

//@Preview(showBackground = true)
@Composable
fun MainPage(apiService: ApiService, sessionManager: SessionManager) {

    val token=sessionManager.getToken()
//    LaunchedEffect(key1 = token){
//        val response = apiService.getCurrentUser("Bearer $token", token)
//        if(response.isSuccessful){
//            sessionManager.saveId(response.body()!!.id)
//        }
//    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .fillMaxHeight()
    ) {
        val textModifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
        Spacer(Modifier.padding(vertical = 60.dp))
        Text(
            text = stringResource(R.string.welcome_title),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = textModifier
        )
        Text(
            text = stringResource(R.string.welcome_subtitle),
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            modifier = textModifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage() {
    val coroutineScope = rememberCoroutineScope()
    val navHostController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val apiService = RetrofitClient.create(sessionManager)
    val isSearchBar = remember { mutableStateOf(true) }
    val isSearchBarActive = remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Drawer(
                navHostController = navHostController,
                drawerState = drawerState,
                coroutineScope = coroutineScope,
                sessionManager = sessionManager
            )
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                VintedduTopAppBar(
                    navHostController = navHostController,
                    drawerState = drawerState,
                    coroutineScope = coroutineScope
                )
            },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it), contentAlignment = Alignment.Center
            ) {
                if (isSearchBar.value) {
                    SearchBar(
                        apiService = apiService,
                        sessionManager = sessionManager,
                        navHostController = navHostController,
                        isSearchBarActive = isSearchBarActive
                    )
                }
                NavigationView(apiService = apiService, sessionManager = sessionManager, navHostController = navHostController, isSearchBar = isSearchBar)
            }
        }
    }
}

@Composable
fun Drawer(
    navHostController: NavHostController,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope,
    sessionManager: SessionManager
) {
    val isLogged = remember { mutableStateOf(sessionManager.isLoggedIn()) }

    val routes = if (!isLogged.value) listOf(Routes.HOME) else listOf(Routes.HOME, Routes.FAVORITES)
    val selectedItem = remember { mutableStateOf(routes[0].icon) }
    ModalDrawerSheet() {
        Card(
            modifier = Modifier
                .padding(30.dp)
                .align(Alignment.CenterHorizontally), shape = ShapeDefaults.Medium
        ) {
            Icon(
                Icons.Rounded.Person, contentDescription = "profile avatar",
                modifier = Modifier
                    .size(80.dp)
                    .clickable {
                        if (isLogged.value) {
                            coroutineScope.launch {
                                drawerState.close()
                                navHostController.navigate(Routes.PROFILE.route) {
                                    popUpTo(Routes.PROFILE.route)
                                }
                            }
                        }
                    }
            )
        }
        if (!isLogged.value) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    navHostController.navigate(Routes.LOGIN.route)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.login))
            }
        }
        Spacer(Modifier.height(12.dp))
        routes.forEach { item ->
            NavigationDrawerItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(stringResource(item.stringName)) },
                selected = item.icon == selectedItem.value,
                onClick = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    navHostController.navigate(item.route) {
                        popUpTo(item.route)
                    }
                    selectedItem.value = item.icon
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    drawerState.close()
                    sessionManager.clearToken()
                    sessionManager.clearUsername()
                }
                navHostController.navigate(Routes.HOME.route)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.login))
        }
    }
}

