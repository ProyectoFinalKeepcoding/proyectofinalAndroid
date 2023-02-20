package com.mockknights.petshelter.ui.login

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mockknights.petshelter.ui.components.FormField
import com.mockknights.petshelter.ui.components.CreateWelcomeButton
import com.mockknights.petshelter.ui.theme.RedKiwoko

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview(showSystemUi = true)
@Composable
fun LoginScreen (viewModel: LoginViewModel = hiltViewModel(),
    navigateToPetShelter: () -> (Unit) = {},
    navigateToWelcome: () -> (Unit)= {},
    navigateToRegister: () -> (Unit) = {}) {

    val success by viewModel.stateLogin.collectAsState()
    LaunchedEffect(key1 = success) {
        if (success.equals(LoginState.Succes("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJjb20ubW9ja2tuaWdodHMucGV0c2hlbHRlciIsInN1YiI6IkFDNTdBNkMzLUQ2RTYtNEY2OC04RkQzLTU2MEU1MkVGRTc2NiJ9.oKwuGiFAjxnQgJn0Az59jfs3JhJOYwz7IJoIOCKLhLs"))) {
            navigateToPetShelter()
        }
    }


    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar() {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver atras",
                modifier = Modifier.clickable { navigateToWelcome() })
            Text(text = "BACK")
        }
    }) {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            LoginForm(viewModel = viewModel)
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp))
            Register(navigateToRegister)
        }
    }

}

@Composable
fun LoginForm(extended: Boolean = true, viewModel: LoginViewModel) {
    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        var user by remember { mutableStateOf("isma") }
        var password by remember { mutableStateOf("123456") }

        FormField(
            value = user,
            onValueChange = { user = it },
            placeholder = "Email")

        if (extended) {
            FormField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password")
        }
        CreateWelcomeButton(name = "LOGIN", modifier = Modifier.fillMaxWidth(),colorButton = RedKiwoko, colorText = Color.White) {
            viewModel.getToken(user, password)
        }
    }
}

@Composable
fun Register(navigateToRegister: () -> (Unit) = {}) {
    Text(text = "¿AUN NO ESTAS REGISTRADO? Registrate", Modifier.clickable{ navigateToRegister() })
}



