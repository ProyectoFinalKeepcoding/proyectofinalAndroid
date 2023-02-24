package com.mockknights.petshelter.ui.register

import com.mockknights.petshelter.ui.login.LoginState

sealed class RegisterState {
    data class Succes(val state: Boolean): RegisterState()
    object loading: RegisterState()
}
