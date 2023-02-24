package com.mockknights.petshelter.ui.login


sealed class LoginState {
    data class Succes(val token: String): LoginState()
    data class Failure (val error: String?): LoginState()
    data class NetworkError (val code: Int): LoginState()
    object loading: LoginState()


}