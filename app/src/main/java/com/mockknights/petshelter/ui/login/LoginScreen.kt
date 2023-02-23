package com.mockknights.petshelter.ui.login

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density.toInt())
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density.toInt())

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview(showSystemUi = true)
@Composable
fun LoginScreen (
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToDetail: (String) -> Unit = {},
    navigateToWelcome: () -> (Unit)= {},
    navigateToRegister: () -> (Unit) = {}) {

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
                .padding(25.toDp().dp),
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
            )
        }
    }

}

@Preview
@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    success: LoginState = LoginState.loading,
    onTokenRequested: (String, String) -> Unit = {_, _ -> (Unit)},
    navigateToDetail: (String) -> (Unit) = {}
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.toDp().dp),
        modifier = modifier
            .fillMaxWidth()
    ) {

        var user by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        BoldTitle(
            title = "Ya tengo cuenta"
        )

        UserDataFieldTextField(
            userData = user,
            placeholderText = "Usuario",
            doneAction = ImeAction.Next,
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

@Preview
@Composable
fun EnterButton(success: LoginState = LoginState.loading,
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

@Preview
@Composable
fun Register(modifier: Modifier = Modifier, navigateToRegister: () -> (Unit) = {}) {
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



