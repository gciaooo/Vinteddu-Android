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
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.unical.gciaoo.vinteddu_android.model.Item
import it.unical.gciaoo.vinteddu_android.ui.theme.Typography


@Composable
fun ItemPage(item : Item) {
    val buttonModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp, vertical = 4.dp)
    val dividerModifier = Modifier.padding(vertical = 10.dp)
    val imagesState = rememberLazyListState()

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top, modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(item.name, style = Typography.headlineLarge, modifier = Modifier.align(Alignment.Start))

        LazyRow(state = imagesState, modifier = Modifier.padding(30.dp)) {
            items(item.images) {
                Box(modifier = Modifier.height(100.dp)) {
                    Image(bitmap = it.asImageBitmap(), contentDescription = stringResource(R.string.item_image))
                }
            }
        }
        Text(item.price.toPlainString(), style = Typography.titleLarge)
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
        Text(item.description, style = Typography.bodySmall)
        Divider(modifier = dividerModifier)
    }
}