package com.mockknights.petshelter.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mockknights.petshelter.ui.components.FormField
import com.mockknights.petshelter.ui.components.createButton

@Preview(showSystemUi = true)
@Composable
fun LoginScreen (viewModel: LoginViewModel = hiltViewModel(), navigateToPetShelter: () -> (Unit) = {}, navigateToWelcome: () -> (Unit)= {}) {

    val success by viewModel.stateLogin.collectAsState()
    LaunchedEffect(key1 = success) {
        //TODO: Tema de LIVE DATA CON FLOW
        if(false) {
            navigateToPetShelter()
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize(), 
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TopAppBar() {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver atras",
                modifier = Modifier.clickable { navigateToWelcome() })
            Text(text = "PANTALLA DE HEROES")
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(200.dp))

        LoginForm(viewModel = viewModel)
    }
}

@Composable
fun LoginForm(extended: Boolean = true, viewModel: LoginViewModel) {
    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        var user by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

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
        createButton(name = "LOGIN", color = Color.Black) {
            viewModel.getToken(user, password)
        }
    }
}



