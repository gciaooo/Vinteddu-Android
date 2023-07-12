package it.unical.gciaoo.vinteddu_android

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.DisposableEffect
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.LocalActivityManager
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import java.io.File


@ExperimentalMaterial3Api
@Composable
fun SearchBar(
    apiService: ApiService,
    sessionManager: SessionManager,
    navHostController: NavHostController,
    isSearchBarActive: MutableState<Boolean>,
    isLogged: MutableState<Boolean>
) {
    val coroutineScope = rememberCoroutineScope()
    var text by rememberSaveable { mutableStateOf("") }
    val active = remember { mutableStateOf(false) }
    val token = sessionManager.getToken()
    val searchResult = remember { mutableListOf<Item>() }
    val showResult = remember { mutableStateOf(false) }
    val showError = remember { mutableStateOf(false) }

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
                    if(isLogged.value) {
                        coroutineScope.launch {
                            val res = apiService.getSearch("Bearer $token", text, token)
                            if (res.isSuccessful) {
                                for (item in res.body()!!) {
                                    searchResult.add(item)
                                }
                                showResult.value = true
                                text = ""
                            }
                        }
                    }else{
                        showError.value=true
                    }
                },
                active = active.value,
                onActiveChange = {
                    active.value = it
                    if (active.value) {
                        navHostController.navigate(Routes.SEARCH.route)
                    }
                    else {
                        navHostController.popBackStack()
                    }
                },
                placeholder = { Text(stringResource(R.string.search_bar_placeholder)) },
                leadingIcon = {
                    if (!active.value)
                        Icon(Icons.Default.Search, contentDescription = null)
                    else
                        IconButton(onClick = {
                            active.value = false
                            searchResult.clear()
                            showResult.value= false
                            navHostController.popBackStack()
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                        }},
                trailingIcon = {
                    if (active.value && text.isNotEmpty()) IconButton(onClick = {
                        searchResult.clear()
                        showResult.value= false
                        text = ""
                    }
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            ) {
                if(showError.value) {
                    AlertDialog(
                        onDismissRequest = {
                            showError.value = false
                        },
                        title = {
                            Text(text = "Devi effettuare l'accesso per poter usufruire delle funzioni di Vinteddu")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    navHostController.navigate(Routes.LOGIN.route)
                                    showError.value = false // Chiudi il popup
                                }
                            ) {
                                Text(text = "OK")
                            }
                        },
                        modifier = Modifier.padding(16.dp)
                    )
                }
                if(showResult.value) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                //active.value = false
                                //navHostController.navigate(Routes.SEARCH.route)
                            },
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        items(searchResult) { item ->
                            val itemModifier = Modifier.clickable {
                            }
                            ItemPreview(
                                item.id,
                                itemModifier,
                                item.nome,
                                navHostController,
                                active,
                                searchResult,
                                showResult
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun ItemPage(apiService: ApiService, sessionManager: SessionManager, itemId: Long, navHostController:NavHostController) {
    val buttonModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp, vertical = 4.dp)
    val dividerModifier = Modifier.padding(vertical = 10.dp)


    val token = sessionManager.getToken()
    val coroutineScope = rememberCoroutineScope()
    val showDialog = remember { mutableStateOf(false) }
    val showDialog2 = remember { mutableStateOf(false) }
    val showDialog3 = remember { mutableStateOf(false) }
    val item = remember { mutableStateOf<Item?>(null) }
    val imageUri = remember { mutableStateOf<Uri?>(null)}
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null)}



    LaunchedEffect(key1 = token/*, key2 = item.value*/){
        val response = apiService.getItem("Bearer $token", itemId)
        sleep(20)
        if(response.isSuccessful){
            imageUri.value = response.body()!!.immagini?.toUri()
            item.value = response.body()
        }
    }




    if(item.value!=null){
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top, modifier = Modifier.padding(horizontal = 20.dp)) {
//            imageUri.value?.let { uri->
//                val source = ImageDecoder.createSource(context.contentResolver, uri)
//                bitmap.value = ImageDecoder.decodeBitmap(source)
//
//                bitmap.value?.let { btm ->
//                    Image(
//
//                        bitmap = btm.asImageBitmap(),
//                        contentDescription = null,
//                        modifier = Modifier.size(400.dp).padding(20.dp)
//
//
//                    )
//                }
//            }

            if(item.value!!.immagini=="null"){

                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(10.dp)

                )

            }
            else{
                Image(
                    painter = rememberImagePainter(
                        data = item.value!!.immagini,
                        builder = {
                            crossfade(true)
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.FillBounds
                )
            }

//            imageUri.value?.let { uri ->
//                Image(
//                    painter = rememberImagePainter(data = uri),
//                    contentDescription = null,
//                    modifier = Modifier.size(400.dp).padding(20.dp)
//                )
//            }


            Text(item.value!!.nome, style = Typography.headlineLarge, modifier = Modifier.align(Alignment.Start))


           // Text(item.value!!.price.toPlainString(), style = Typography.titleLarge)
            Button(onClick = {
                coroutineScope.launch {
                    try {
                        val res = apiService.saldo("Bearer $token", itemId, token)
                        if(res.isSuccessful){
                            showDialog.value = true
                        }else{
                            showDialog2.value=true
                        }

                    }catch(e: Exception ){
                        e.printStackTrace()
                    }
                }
                             }, modifier = buttonModifier) {
                Text(stringResource(id = R.string.purchase))
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = stringResource(R.string.purchase_icon)
                )
            }
            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        showDialog.value = false
                    },
                    title = {
                        Text(text = "Vuoi acquistare questo oggetto?" +
                        item.value!!.nome+
                        "  $"+item.value!!.prezzo)
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    val response = apiService.buyItem("Bearer $token", itemId, token)
                                }
                                showDialog.value = false // Chiudi il popup
                                navHostController.navigate(Routes.HOME.route)
                            }
                        ) {
                            Text(text = "Sì")
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
                        Text(text = "Non hai abbastanza fondi")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDialog2.value = false // Chiudi il popup
                            }
                        ) {
                            Text(text = "OK")
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }
            FilledTonalButton(
                onClick = {
                          coroutineScope.launch {
                              try {
                                  showDialog3.value = true
                                  val response = apiService.addFavorites("Bearer $token", itemId, token)
                              }catch(e: Exception){
                                  //errore
                              }
                          }
            }, modifier = buttonModifier) {
                Text(stringResource(R.string.add_to_wishlist))
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = stringResource(R.string.add_to_wishlist_icon)
                )
            }
            if (showDialog3.value) {
                AlertDialog(
                    onDismissRequest = {
                        showDialog3.value = false
                    },
                    title = {
                        Text(text = "Aggiunto ai preferiti")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDialog3.value = false // Chiudi il popup
                            }
                        ) {
                            Text(text = "OK")
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Text(stringResource(R.string.description), style = Typography.titleSmall, modifier = Modifier.align(Alignment.Start))
            Divider(modifier = dividerModifier)
            item.value!!.descrizione?.let { Text(it, style = Typography.bodySmall) }
            Divider(modifier = dividerModifier)
        }

    }
    else{
        sleep(1000)
    }

}

@Composable
fun ItemPreview(
    id: Long?,
    modifier: Modifier,
    name: String,
    navHostController: NavHostController,
    active: MutableState<Boolean>,
    searchResult: MutableList<Item>,
    showResult: MutableState<Boolean>
) {
    val itemImage by remember { mutableStateOf<Bitmap?>(null)}
    val coroutineScope = rememberCoroutineScope()


    //val itemName by remember { mutableStateOf<String?>(null) }
    ListItem(
        headlineContent = {
            //Text(name ?: stringResource(R.string.loading_wait))
            ClickableText(
                text = AnnotatedString(name),
                onClick = {
                    coroutineScope.launch {
                        active.value=false
                        showResult.value=false
                        searchResult.clear()
                        navHostController.navigate("items/${id}")
                    }


                },
                style = MaterialTheme.typography.bodyMedium
            )
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















