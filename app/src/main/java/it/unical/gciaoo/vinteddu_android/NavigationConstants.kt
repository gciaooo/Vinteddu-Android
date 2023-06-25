package it.unical.gciaoo.vinteddu_android

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class Routes(val route: String, val icon: ImageVector, val stringName : Int) {
    HOME("/", Icons.Filled.Home, R.string.home),
    LOGIN("login", Icons.Filled.Person, R.string.login),
    REGISTER("login/new", Icons.Filled.ExitToApp, R.string.register),
    PROFILE("profile", Icons.Filled.Person, R.string.user_page);
}
