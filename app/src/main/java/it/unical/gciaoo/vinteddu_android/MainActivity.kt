package it.unical.gciaoo.vinteddu_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import it.unical.gciaoo.vinteddu_android.ui.theme.VintedduAndroidTheme
import it.unical.gciaoo.vinteddu_android.viewmodels.AddressFormViewModel
import it.unical.gciaoo.vinteddu_android.viewmodels.UserFormViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient


class MainActivity : ComponentActivity() {
    private lateinit var sessionManager: SessionManager
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
    NavHost(navController = navHostController, startDestination = Routes.HOME.route) {
        composable(Routes.HOME.route) {
            //TODO: Main page
            Box {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
        composable(Routes.LOGIN.route) {
            val context = LocalContext.current
            val sessionManager = remember { SessionManager(context) }
            Login(navHostController = navHostController, apiService = RetrofitClient.create(sessionManager))
        }
        composable(Routes.REGISTER.route) {
            Register(
                userFormViewModel = UserFormViewModel(),
                addressFormViewModel = AddressFormViewModel()
            )
        }
        composable(Routes.PROFILE.route) {
            val context = LocalContext.current
            val sessionManager = remember { SessionManager(context) }
            Profile(userFormViewModel = UserFormViewModel(), apiService = RetrofitClient.create(sessionManager), sessionManager = sessionManager)
        }

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

        actions = {
            IconButton(onClick = { navHostController.navigate(Routes.PROFILE.route) }) {
                Icon(Icons.Filled.Person, contentDescription = stringResource(R.string.profile))
            }
        }

    )
}

@Composable
fun HomePage() {
    val coroutineScope = rememberCoroutineScope()
    val navHostController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Drawer(
                navHostController = navHostController,
                drawerState = drawerState,
                coroutineScope = coroutineScope
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
                NavigationView(navHostController = navHostController)
            }
        }
    }
}

@Composable
fun Drawer(
    navHostController: NavHostController,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope
) {
    //TODO: remove login item from drawer
    val routes = listOf(Routes.HOME, Routes.LOGIN)
    val selectedItem = remember { mutableStateOf(routes[0].icon) }
    ModalDrawerSheet() {
        Card(
            modifier = Modifier
                .padding(30.dp)
                .align(Alignment.CenterHorizontally), shape = ShapeDefaults.Medium
        ) {
            Icon(
                Icons.Rounded.Person, contentDescription = "profile avatar",
                modifier = Modifier.size(80.dp)
            )
        }
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
    }
}

fun getUsernameFromToken(token: String?): String? {
//    val claims: Claims = Jwts.parser().parseClaimsJwt(token).body
//    return claims.subject
    val claims: Claims = Jwts.parser().parseClaimsJws(token).body
    return claims.subject
}
