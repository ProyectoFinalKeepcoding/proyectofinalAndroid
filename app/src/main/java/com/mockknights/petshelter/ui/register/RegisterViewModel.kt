package com.mockknights.petshelter.ui.register

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
     * Registers the register request. If the register is successful, the state is set to [RegisterState.Success].
     * If the register is not successful, the state is set to [RegisterState.Failure].
     */
    fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            try {
                repository.register(registerRequest)
                setValueOnMainThread(RegisterState.Success)
            } catch (e: Exception) {
                setValueOnMainThread(RegisterState.Failure(error = e.message.toString()))
            }
        }
    }
}
