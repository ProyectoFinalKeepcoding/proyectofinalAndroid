package com.mockknights.petshelter.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mockknights.petshelter.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _stateLogin = MutableStateFlow<LoginState>(LoginState.loading)
    val stateLogin: StateFlow<LoginState> get() = _stateLogin

    private fun setValueOnMainThread(value: LoginState) {
        viewModelScope.launch(Dispatchers.Main) {
            _stateLogin.value = value
        }
    }

    fun getToken(user: String, password: String) {

        viewModelScope.launch {
            repository.getToken().flowOn(Dispatchers.IO).collect() {
                setValueOnMainThread(LoginState.Succes(it))
                }
        }
    }

}