package com.mockknights.petshelter.ui.login

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mockknights.petshelter.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    val sharedPreferences: SharedPreferences
): ViewModel()
{

    private val _stateLogin = MutableStateFlow<LoginState>(LoginState.loading)
    val stateLogin: StateFlow<LoginState> get() = _stateLogin

    private fun setValueOnMainThread(value: LoginState) {
        viewModelScope.launch(Dispatchers.Main) {
            _stateLogin.value = value
        }
    }

    private fun getCredentials(user: String, pass: String): String {
        return Credentials.basic(user, pass, StandardCharsets.UTF_8)
    }


    fun getToken(user: String, password: String) {

        if (sharedPreferences.getString("TOKEN", null) == null) {
            sharedPreferences.edit().putString("CREDENTIAL", getCredentials(user, password)).apply()
        }

        viewModelScope.launch {
            repository.getToken().flowOn(Dispatchers.IO).collect() {
                setValueOnMainThread(LoginState.Succes(it))
                }
        }
    }

}