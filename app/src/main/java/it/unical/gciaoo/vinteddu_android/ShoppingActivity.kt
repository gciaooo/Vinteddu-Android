package it.unical.gciaoo.vinteddu_android

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.unical.gciaoo.vinteddu_android.ApiConfig.ApiService
import it.unical.gciaoo.vinteddu_android.ApiConfig.SessionManager
import it.unical.gciaoo.vinteddu_android.model.Item
import it.unical.gciaoo.vinteddu_android.ui.theme.Typography
import okhttp3.internal.wait
import java.lang.Thread.sleep


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
                Text(stringResource(id = R.string.add_to_cart))
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = stringResource(R.string.add_to_cart_icon)
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


