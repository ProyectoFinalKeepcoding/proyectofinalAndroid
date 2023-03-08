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

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _registerLogin = MutableStateFlow<RegisterState>(RegisterState.loading)
    val registerLogin: StateFlow<RegisterState> get() = _registerLogin

    private fun setValueOnMainThread(value: RegisterState) {
        viewModelScope.launch(Dispatchers.Main) {
            _registerLogin.value = value
        }
    }

    fun resetState() {
        setValueOnMainThread(RegisterState.loading)
    }



    fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            try {
                repository.register(registerRequest)
                setValueOnMainThread(RegisterState.Success("REGISTRADO CORRECTAMENTE"))


            } catch (e: Exception) {
                setValueOnMainThread(RegisterState.Failure(error = e.message.toString()))
                Log.d("RegisterViewModel", e.message.toString())
            }
        }
    }

    // Function to generate a Toast
    fun mToast(context: Context, error: String){
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }
}
