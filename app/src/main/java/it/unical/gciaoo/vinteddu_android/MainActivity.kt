package it.unical.gciaoo.vinteddu_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import it.unical.gciaoo.vinteddu_android.ui.theme.VintedduAndroidTheme

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
            Register()
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
        }
    )
}
@Preview
@Composable
fun HomePage() {
    val navHostController = rememberNavController()

    Scaffold(topBar = { VintedduTopAppBar(navHostController = navHostController) }) {
        Box(modifier = Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center) {
            NavigationView(navHostController = navHostController)
        }
    }
}