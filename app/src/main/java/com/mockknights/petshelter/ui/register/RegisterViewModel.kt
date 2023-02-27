package com.mockknights.petshelter.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mockknights.petshelter.data.remote.request.RegisterRequest
import com.mockknights.petshelter.domain.Repository
import com.mockknights.petshelter.ui.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
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



    fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            repository.register(registerRequest)
        }
    }
}
