package com.mockknights.petshelter.ui.register

sealed class RegisterState {
    object Success: RegisterState()
    data class Failure (val error: String?): RegisterState()
    object Loading: RegisterState()
}
