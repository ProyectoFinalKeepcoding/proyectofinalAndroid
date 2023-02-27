package com.mockknights.petshelter.ui.register

sealed class RegisterState {
    data class Succes(val state: Boolean): RegisterState()
    object loading: RegisterState()
}
