package it.unical.gciaoo.vinteddu_android

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.unical.gciaoo.vinteddu_android.ui.theme.Typography

@Preview(showBackground = true)
@Composable
fun Login() {
    Column(modifier = Modifier.padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
        stringResource(R.string.login), style = Typography.headlineLarge,
        modifier = Modifier
            .align(Alignment.Start)
            .padding(vertical = 50.dp, horizontal = 10.dp))
        InputField(name = stringResource(R.string.login_username))
        InputField(name = stringResource(R.string.login_password))
        Button(content = {
            Text(stringResource(R.string.login))
        },
            modifier = Modifier.padding(vertical = 30.dp),
        onClick = {}
        )
        Text(stringResource(R.string.register), modifier = Modifier.clickable {})
    }
}

@Composable
fun InputField(name: String) {
    var field by remember { mutableStateOf("") }
    Row(horizontalArrangement = Arrangement.Center) {
        TextField(
            value = field,
            onValueChange = {field = it},
            label = {Text(name)},
            singleLine = true,
            modifier = Modifier.padding(vertical = 10.dp),
        )
    }
}