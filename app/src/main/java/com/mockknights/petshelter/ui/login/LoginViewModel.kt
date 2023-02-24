package com.mockknights.petshelter.ui.login

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mockknights.petshelter.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

    fun resetState() {
        setValueOnMainThread(LoginState.loading)
    }

    private fun getCredentials(user: String, pass: String): String {
        return Credentials.basic(user, pass, StandardCharsets.UTF_8)
    }


    fun getToken(user: String, password: String) {
        setValueOnMainThread(LoginState.loading)
        // Delete shared preferences for key CREDENTIAL
        sharedPreferences.edit().remove("CREDENTIAL").apply() // TODO: Manage login when the user has already logged in
        sharedPreferences.edit().remove("TOKEN").apply()
        if (sharedPreferences.getString("TOKEN", null) == null) {
            sharedPreferences.edit().putString("CREDENTIAL", getCredentials(user, password)).apply()
        }

        viewModelScope.launch {
            repository.getToken().flowOn(Dispatchers.IO).collect() { tokenAndId ->
                if(tokenAndId.isNotEmpty()) setValueOnMainThread(LoginState.Success(token = tokenAndId[0], id = tokenAndId[1]))
                else setValueOnMainThread(LoginState.Failure(error = "Error while retrieving the token: Token is empty"))
            }
        }
    }

}