package com.mockknights.petshelter.ui.register

sealed class RegisterState {
    data class Success(val state: Boolean): RegisterState()
    data class Failure (val error: String?): RegisterState()
    object loading: RegisterState()
}
