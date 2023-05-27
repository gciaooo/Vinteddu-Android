package it.unical.gciaoo.vinteddu_android

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unical.gciaoo.vinteddu_android.ui.theme.Typography


@Composable
fun Login(navHostController: NavHostController) {
    Column(
        modifier = Modifier.padding(all = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            text = stringResource(R.string.login), style = Typography.headlineLarge,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 10.dp)
                .weight(0.3f)
        )
        InputField(name = stringResource(R.string.login_username))
        InputField(name = stringResource(R.string.login_password))
        Button(content = {
                Text(stringResource(R.string.login))

        },
            modifier = Modifier.padding(vertical = 30.dp),
            onClick = {

            }
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            stringResource(R.string.register_prompt),
            modifier = Modifier.clickable(onClick = { navHostController.navigate("login/new") }).padding(vertical = 10.dp)
        )
    }
}

@Composable
fun Register() {
    //TODO: implement Register Widget
}

@Composable
fun InputField(name: String) {
    var field by remember { mutableStateOf("") }
    Row(horizontalArrangement = Arrangement.Center) {
        TextField(
            value = field,
            onValueChange = { field = it },
            label = { Text(name) },
            singleLine = true,
            modifier = Modifier.padding(vertical = 10.dp)
        )
    }
}