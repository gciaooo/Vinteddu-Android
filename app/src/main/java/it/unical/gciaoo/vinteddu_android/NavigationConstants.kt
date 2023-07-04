package it.unical.gciaoo.vinteddu_android

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class Routes(val route: String, val icon: ImageVector, val stringName : Int) {
    HOME("/", Icons.Default.Home, R.string.home),
    LOGIN("login", Icons.Default.Person, R.string.login),
    REGISTER("login/new", Icons.Default.ExitToApp, R.string.register),
    PROFILE("profile", Icons.Default.Person, R.string.user_page),
    SEARCH("search", Icons.Default.Search, R.string.search),
    LOSTPASSWORD("login/recovery", Icons.Default.Place, R.string.lost_password),
    ITEMS("items/{id}", Icons.Default.List, R.string.item_preview_icon),
}
