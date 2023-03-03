package com.mockknights.petshelter.ui.login

/**
 * This class represents the state of the login data.
 */
sealed class LoginState {
    /**
     * This class represents the state of the login data when it is successfully loaded. The
     * [token] property contains the token and the [id] property contains the id of the shelter.
     */
    data class Success(val token: String, val id: String): LoginState()
    /**
     * This class represents the state of the login data when it is not successfully loaded. The
     * [error] property contains the error message.
     */
    data class Failure (val error: String?): LoginState()
    /**
     * This class represents the state of the login data when it is Loading. It is the default state
     * of the login data.
     */
    object Loading: LoginState()
}