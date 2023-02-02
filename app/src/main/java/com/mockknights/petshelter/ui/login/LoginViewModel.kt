package com.mockknights.petshelter.ui.login

import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mockknights.petshelter.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _stateLogin = MutableLiveData<LoginState>()
    val stateLogin: LiveData<LoginState>
        get() = _stateLogin

    private fun setValueOnMainThread(value: LoginState) {
        viewModelScope.launch(Dispatchers.Main) {
            _stateLogin.value = value
        }
    }

    fun getToken(user: String, password: String) {
        setValueOnMainThread(LoginState.loading)

        if (!checkUser(user)) {
            setValueOnMainThread(LoginState.Failure("Error en Usuario"))
            return
        }

        if (!checkPassword(password)) {
            setValueOnMainThread(LoginState.Failure("Error en Contrasenya"))
            return
        }


        viewModelScope.launch {
            val token = withContext(Dispatchers.IO) {
                //TODO: LLAMADA AL TOKEN
                //repository.getToken()
                "true"
            }
            setValueOnMainThread(LoginState.Succes(token))
        }
    }



    private fun checkUser(user: String): Boolean {
        if (user.isEmpty() || user.isBlank()) return false
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(user).matches()) return false
        return true
    }

    private fun checkPassword(password: String): Boolean {
        val passwordRegex = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +   //por lo menos 1 digito
                    "(?=.*[a-z])" +     //1 letra minuscula
                    "(?=.*[A-Z])" +     //1 letra mayuscula
                    "(?=\\S+$)" +     //no espacios en blanco
                    ".{4,}" +     //por lo menos 4 caracteres
                    "$"
        )
        if (password.isEmpty() || password.isBlank()) return false
        if (!passwordRegex.matcher(password).matches()) return false
        return true
    }
}