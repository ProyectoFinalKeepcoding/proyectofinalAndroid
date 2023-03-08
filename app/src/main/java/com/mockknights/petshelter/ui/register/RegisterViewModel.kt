package com.mockknights.petshelter.ui.register

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mockknights.petshelter.data.remote.request.RegisterRequest
import com.mockknights.petshelter.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Register screen.
 * The ViewModel works with the Repository to manage the data.
 * @param repository The repository that manages the data
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    /**
     * The state of the register screen. When working with the register state inside the register view model, use this value.
     */
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Loading)

    /**
     * The state of the register screen visible outside the register view model.
     */
    val registerState: StateFlow<RegisterState> get() = _registerState

    /**
     * Set the value of [_registerState] on the main thread.
     */
    private fun setValueOnMainThread(value: RegisterState) {
        viewModelScope.launch(Dispatchers.Main) {
            _registerState.value = value
        }
    }

    /**
     * Resets the state of the register screen to [RegisterState.Loading].
     */
    fun resetState() {
        setValueOnMainThread(RegisterState.Loading)
    }

    /**
     * Registers the register request. If the register is successful, the state is set to [RegisterState.Success].
     * If the register is not successful, the state is set to [RegisterState.Failure].
     */
    fun register(registerRequest: RegisterRequest, context: Context) {
        viewModelScope.launch {
            try {
                if(checkEmptyProperties(registerRequest, context)) {
                    repository.register(registerRequest)
                    setValueOnMainThread(RegisterState.Success("REGISTRADO CORRECTAMENTE"))
                }
            } catch (e: Exception) {
                setValueOnMainThread(RegisterState.Failure(error = "NOMBRE DE USUARIO YA EXISTENTE"))
            }
        }
    }

    private fun checkEmptyProperties(registerRequest: RegisterRequest, context: Context): Boolean {
        if(registerRequest.name.isEmpty()){
            mToast(context, "NOMBRE DE USUARIO VACÍO")
            return false
        }
        if (registerRequest.password.isEmpty()){
            mToast(context, "CONTRASEÑA VACÍA")
            return false
        }
        if (registerRequest.address.latitude == 0.0 && registerRequest.address.longitude == 0.0){
            mToast(context, "DIRECCIÓN NO VÁLIDA")
            return false
        }
        if(registerRequest.phoneNumber.isEmpty()){
            mToast(context, "TELÉFONO VACÍO")
            return false
        }
        return true
    }

    /**
     * Makes a toast on UI thread.
     * @param context The context of the activity.
     * @param error The error message to show.
     */
    fun mToast(context: Context, error: String){
        viewModelScope.launch {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }
}
