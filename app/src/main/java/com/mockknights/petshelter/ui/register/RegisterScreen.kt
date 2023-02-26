package com.mockknights.petshelter.ui.register

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.mockknights.petshelter.ui.components.CreateWelcomeButton
import com.mockknights.petshelter.ui.theme.RedKiwoko

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview(showSystemUi = true)
@Composable
fun RegisterScreen (viewModel: RegisterViewModel = hiltViewModel()) {

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar() {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver atras",
                modifier = Modifier.clickable { })
            Text(text = "Login")
        }
    }) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            RegisterForm()
        }
    }
}


@Composable
fun RegisterForm() {

    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }


//    Text(text = "Crear una nueva cuenta")
//    Text(text = "Usuario")
//    FormField(value = user, onValueChange = { user = it }, placeholder = "Usuario")
//
//    Text(text = "Contraseña")
//    FormField(value = password, onValueChange = {password = it }, placeholder = "Contraseña", isPassword = true)
//
//    Text(text = "Direccion")
//    FormField(value = direccion, onValueChange = {direccion = it }, placeholder = "Direccion")
//
//    Text(text = "Telefono")
//    FormField(value = telefono, onValueChange = {telefono = it }, placeholder = "Numero de Telefono")


    CreateWelcomeButton(name = "Crear cuenta", modifier = Modifier.fillMaxWidth(), colorButton = RedKiwoko, colorText = Color.White) {

    }
}



