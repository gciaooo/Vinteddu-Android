package it.unical.gciaoo.vinteddu_android

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.unical.gciaoo.vinteddu_android.ApiConfig.ApiService
import it.unical.gciaoo.vinteddu_android.ApiConfig.SessionManager
import it.unical.gciaoo.vinteddu_android.model.Item
import it.unical.gciaoo.vinteddu_android.ui.theme.Typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import java.lang.Thread.sleep

@ExperimentalMaterial3Api
@Composable
fun SearchBar(
    apiService: ApiService,
    sessionManager: SessionManager,
    navHostController: NavHostController,
) {
    val coroutineScope = rememberCoroutineScope()
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
//    var searches = remember { mutableListOf<Item>() }
    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .semantics {
                    @Suppress("DEPRECATION")
                    isContainer = true
                }
                .zIndex(1f)
                .fillMaxWidth()) {
            SearchBar(
                modifier = Modifier.align(Alignment.TopCenter),
                query = text,
                onQueryChange = { text = it },
                onSearch = {
//                    coroutineScope.launch {
//                        val res = apiService.search("Bearer ${sessionManager.getToken()}", text)
//                        searches = res.body()
//                    }
                },
                active = active,
                onActiveChange = {
                    active = it
                },
                placeholder = { Text(stringResource(R.string.search_bar_placeholder)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
//                    val l = listOf("1", "2", "3", "4")
//                    items(l) { idx ->
//                        val itemModifier = Modifier.clickable {
//                        }
//                        ItemPreview(idx, itemModifier)
//                    }
                }
            }
        }
    }
}

@Composable
fun ItemPage(apiService: ApiService, sessionManager: SessionManager) {
    val buttonModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp, vertical = 4.dp)
    val dividerModifier = Modifier.padding(vertical = 10.dp)
    val imagesState = rememberLazyListState()

    val token = sessionManager.getToken();

    val item = remember { mutableStateOf<Item?>(null) }


    LaunchedEffect(key1 = token/*, key2 = item.value*/){
        val response = apiService.getItem("Bearer $token", 1)
        sleep(20)
        if(response.isSuccessful){
            item.value = response.body()
        }
    }




    if(item.value!=null){
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top, modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(item.value!!.nome, style = Typography.headlineLarge, modifier = Modifier.align(Alignment.Start))

            LazyRow(state = imagesState, modifier = Modifier.padding(30.dp)) {
//                items(item.value!!.images) {
//                    Box(modifier = Modifier.height(100.dp)) {
//                        Image(bitmap = it.asImageBitmap(), contentDescription = stringResource(R.string.item_image))
//                    }
//                }
            }
           // Text(item.value!!.price.toPlainString(), style = Typography.titleLarge)
            Button(onClick = { /*TODO*/ }, modifier = buttonModifier) {
                Text(stringResource(id = R.string.purchase))
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = stringResource(R.string.purchase_icon)
                )
            }
            FilledTonalButton(onClick = { /*TODO*/ }, modifier = buttonModifier) {
                Text(stringResource(R.string.add_to_wishlist))
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = stringResource(R.string.add_to_wishlist_icon)
                )
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Text(stringResource(R.string.description), style = Typography.titleSmall, modifier = Modifier.align(Alignment.Start))
            Divider(modifier = dividerModifier)
            Text(item.value!!.descrizione, style = Typography.bodySmall)
            Divider(modifier = dividerModifier)
        }

    }
    else{
        sleep(1000)
    }

}

@Composable
fun ItemPreview(id: String, modifier: Modifier) {
    val itemImage by remember { mutableStateOf<Bitmap?>(null)}
    val itemName by remember { mutableStateOf<String?>(null) }
    ListItem(
        headlineContent = {
            Text(itemName ?: stringResource(R.string.loading_wait))
        },
        leadingContent = {
            val iconName = R.string.item_preview_icon
            if (itemImage != null) {
//                Icon(itemImage.asImageBitmap(), contentDescription = stringResource(iconName))
            }
            else {
                Icon(Icons.Outlined.Refresh, contentDescription = stringResource(iconName))
            }
        },
        modifier = modifier
    )
}