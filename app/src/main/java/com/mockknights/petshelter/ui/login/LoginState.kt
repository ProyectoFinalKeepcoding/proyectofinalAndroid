package com.mockknights.petshelter.ui.login

import com.mockknights.petshelter.navigation.Screens

sealed class LoginState {
    data class Succes(val token: String): LoginState()
    data class Failure (val error: String?): LoginState()
    data class NetworkError (val code: Int): LoginState()
    object loading: LoginState()


}