package com.mockknights.petshelter.ui.login

<<<<<<< HEAD

=======
>>>>>>> develop
sealed class LoginState {
    data class Success(val token: String, val id: String): LoginState()
    data class Failure (val error: String?): LoginState()
    data class NetworkError (val code: Int): LoginState()
    object loading: LoginState()


}