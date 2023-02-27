package com.mockknights.petshelter.ui.register

import android.annotation.SuppressLint
import android.provider.Telephony.Mms.Addr
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mockknights.petshelter.data.remote.request.RegisterRequest
import com.mockknights.petshelter.data.remote.response.Address
import com.mockknights.petshelter.domain.ShelterType
import com.mockknights.petshelter.ui.components.*
import com.mockknights.petshelter.ui.detail.toDp
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
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.toDp().dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            RegisterForm(
                onRegister = { registerRequest ->
                    viewModel.register(registerRequest)
                }
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterForm(onRegister: (RegisterRequest) -> Unit = {}) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var address by remember { mutableStateOf(Address(0.0, 0.0)) }
    var phone by remember { mutableStateOf("") }
    var shelterType by remember { mutableStateOf(ShelterType.PARTICULAR) }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.toDp().dp)
    ) {

        BoldTitle(
            title = "Crear una nueva cuenta",
            color = Color.Black
        )

        UserDataField(
            fieldLabel = "Usuario",
            userData = user,
            placeholderText = "Usuario",
            keyboardType = KeyboardType.Text,
            doneAction = ImeAction.Next,
            onUpdateValue = { user = it },
            onDone = { focusManager.moveFocus(FocusDirection.Down) }
        )

        UserDataField(
            fieldLabel = "Contraseña",
            userData = password,
            placeholderText = "Contraseña",
            keyboardType = KeyboardType.Text,
            isPassword = true,
            doneAction = ImeAction.Next,
            onUpdateValue = { password = it },
            onDone = { focusManager.moveFocus(FocusDirection.Down) }
        )

        UserAddressField(
            currentAddress = address,
            onUpdateData = { latitude, longitude ->
                // Only updates when a row from suggestions is selected
                address = Address(latitude.toDouble(), longitude.toDouble())
                focusManager.moveFocus(FocusDirection.Down)
            }
        )

        UserDataField(
            fieldLabel = "Teléfono",
            userData = phone,
            placeholderText = "Teléfono",
            keyboardType = KeyboardType.Phone,
            isPassword = false,
            doneAction = ImeAction.Done,
            onUpdateValue = { phone = it },
            onDone = { keyboardController?.hide() }
        )

        RadioButtonsRow(
            onItemClick = { shelterType = it },
        )

        ButtonRow(
            onClick = { onRegister(RegisterRequest(user, password, address, phone, shelterType)) },
        )
    }
}



