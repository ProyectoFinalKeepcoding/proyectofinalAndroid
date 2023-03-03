package com.mockknights.petshelter.ui.login

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mockknights.petshelter.ui.components.BoldTitle
import com.mockknights.petshelter.ui.components.KiwokoIconButton
import com.mockknights.petshelter.ui.components.LogoBox
import com.mockknights.petshelter.ui.components.UserDataFieldTextField
import com.mockknights.petshelter.ui.detail.toDp
import com.mockknights.petshelter.ui.theme.RedKiwoko
import com.mockknights.petshelter.ui.theme.moderatTextField

/**
 * This class represents the login screen. It is composed by the logo, the login form and the register
 * @param [viewModel] ViewModel of the login screen, injected by Hilt
 * @param [navigateToDetail] Function called to navigate to the detail screen
 * @param [navigateToWelcome] Function called to navigate to the welcome screen
 * @param [navigateToRegister] Function called to navigate to the register screen
 */
@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview(showSystemUi = true)
@Composable
fun LoginScreen (
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToDetail: (String) -> Unit = {},
    navigateToWelcome: () -> (Unit)= {},
    navigateToRegister: () -> (Unit) = {}) {


    val keyboardController = LocalSoftwareKeyboardController.current

    val success by viewModel.stateLogin.collectAsState()
    LaunchedEffect(key1 = success) {
        if (success is LoginState.Success) {
            navigateToDetail((success as LoginState.Success).id)
            viewModel.resetState()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar() {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver atras",
                modifier = Modifier.clickable { navigateToWelcome() })
            Text(text = "Inicio")
        }
    }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.toDp().dp).pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        keyboardController?.hide()
                    })
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            LogoBox(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2.4f)
            )

            LoginForm(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(4.3f),
                success = success,
                navigateToDetail = navigateToDetail,
                onTokenRequested = { user, password ->
                    viewModel.getToken(user, password)
                }
            )
            Register(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(3.3f),
                navigateToRegister = { navigateToRegister() }

            )
        }
    }

}

/**
 * Login form formed by the user data fields and the enter button
 * @param [modifier] Modifier of the form applied to the column
 * @param [success] State of the login data
 * @param [onTokenRequested] Function that is called when the enter button is pressed
 * @param [navigateToDetail] Function that is called when the login data is successfully loaded
 */
@Preview
@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    success: LoginState = LoginState.Loading,
    onTokenRequested: (String, String) -> Unit = {_, _ -> (Unit)},
    navigateToDetail: (String) -> (Unit) = {}
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.toDp().dp),
        modifier = modifier
            .fillMaxWidth()
    ) {

        val focusManager = LocalFocusManager.current

        var user by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        BoldTitle(
            title = "Ya tengo cuenta"
        )

        UserDataFieldTextField(
            userData = user,
            placeholderText = "Usuario",
            doneAction = ImeAction.Next,
            onDone = { focusManager.moveFocus(FocusDirection.Down) },
            onUpdateValue = { user = it },
        )

        UserDataFieldTextField(
            userData = password,
            placeholderText = "Contraseña",
            doneAction = ImeAction.Done,
            isPassword = true,
            onUpdateValue = { password = it },
        )

        EnterButton(
            success = success,
            navigateToDetail = navigateToDetail,
            onTokenRequested = { onTokenRequested(user, password) }
        )

        ForgotPassword()
    }
}

/**
 * Enter button of the login screen
 * @param [success] LoginState that contains the state of the login
 * @param [onTokenRequested] Function that is called when the user clicks on the button
 * @param [navigateToDetail] Function that is called when the user is logged in
 */
@Preview
@Composable
fun EnterButton(success: LoginState = LoginState.Loading,
                onTokenRequested: () -> (Unit) = {},
                navigateToDetail: (String) -> (Unit) = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .weight(2f)
        )
        KiwokoIconButton(
            name = "Entrar",
            icon = 0,
            modifier = Modifier
                .fillMaxWidth()
                .weight(6f)
        ) {
            if(success is LoginState.Success) {
                navigateToDetail(success.id)
            } else {
                onTokenRequested()
            }
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .weight(2f)
        )
    }
}

/**
 * Forgot password column that contains the "¿Has olvidado tu contraseña?" actionable text
 */
@Preview
@Composable
fun ForgotPassword() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.toDp().dp)
    ) {
        Text(
            text = "¿Has olvidado tu contraseña?",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { },
            style = MaterialTheme.typography.moderatTextField,
            color = RedKiwoko,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Register column that contains the "No tengo cuenta" info text and the "Registrarme" actionable
 * text
 * @param modifier Modifier
 * @param navigateToRegister Function that navigates to the register screen when "Registrarme" is
 * clicked
 */
@Preview
@Composable
fun Register(
    modifier: Modifier = Modifier,
    navigateToRegister: () -> (Unit) = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.toDp().dp)
        ) {
            BoldTitle(
                title = "No tengo cuenta"
            )
            BoldTitle(
                title = "Registrarme",
                color = RedKiwoko,
                modifier = Modifier.clickable {
                    navigateToRegister()
                }
            )
        }
    }
}



