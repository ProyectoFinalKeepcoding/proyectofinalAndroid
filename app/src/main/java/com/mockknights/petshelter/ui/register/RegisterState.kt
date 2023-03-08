package com.mockknights.petshelter.ui.register

sealed class RegisterState {
    /**
     * This class represents the state of the register data when it is successfully loaded.
     * @param success The success message.
     */
    data class Success(val success: String): RegisterState()
    /**
     * This class represents the state of the register data when it is not successfully loaded. The
     * [error] property contains the error message.
     * @param error The error message
     */
    data class Failure (val error: String?): RegisterState()
    /**
     * This class represents the state of the register data when it is Loading. It is the default state
     * of the register data.
     */
    object Loading: RegisterState()
}
