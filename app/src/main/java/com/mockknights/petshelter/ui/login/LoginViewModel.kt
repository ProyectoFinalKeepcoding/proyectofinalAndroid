package com.mockknights.petshelter.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mockknights.petshelter.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.Credentials
import java.nio.charset.StandardCharsets
import javax.inject.Inject


/**
 * ViewModel for the Login screen.
 * The ViewModel works with the Repository to manage the data.
 * @param repository The repository that manages the data
 * @param sharedPreferences The shared preferences to store the credentials and token
 * @param coroutineDispatcher The coroutine dispatcher working in the IO thread.
 * It is injected to avoid multiple threads when hardcoding it.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    val sharedPreferences: SharedPreferences,
    private val coroutineDispatcher: CoroutineDispatcher,
): ViewModel()
{

    /**
     * The state of the login screen. When working with the login state inside the login view model, use this value.
     */
    private val _stateLogin = MutableStateFlow<LoginState>(LoginState.Loading)

    /**
     * The state of the login screen visible outside the login view model.
     */
    val stateLogin: StateFlow<LoginState> get() = _stateLogin

    /**
     * Set the value of [_stateLogin] on the main thread.
     */
    private fun setValueOnMainThread(value: LoginState) {
        viewModelScope.launch(Dispatchers.Main) {
            _stateLogin.value = value
        }
    }

    /**
     * Reset the value of [_stateLogin] to [LoginState.Loading].
     */
    fun resetState() {
        setValueOnMainThread(LoginState.Loading)
    }

    /**
     * Get the credentials from the user and password.
     * @param user The user name
     * @param pass The password
     */
    private fun getCredentials(user: String, pass: String): String {
        return Credentials.basic(user, pass, StandardCharsets.UTF_8)
    }

    /**
     * Get the token from the repository and set the value of [_stateLogin] to [LoginState.Success]
     * with the gotten token. If the token is not found, the value of [_stateLogin] is set to
     * [LoginState.Failure] with the following error message: "No token found".
     * @param user The user name
     * @param password The password
     */
    fun getToken(user: String, password: String) {
        setValueOnMainThread(LoginState.Loading)
        // Delete shared preferences for key CREDENTIAL
        sharedPreferences.edit().remove("CREDENTIAL").apply() // TODO: Manage login when the user has already logged in
        sharedPreferences.edit().remove("TOKEN").apply()
        if (sharedPreferences.getString("TOKEN", null) == null) {
            sharedPreferences.edit().putString("CREDENTIAL", getCredentials(user, password)).apply()
        }

        viewModelScope.launch {
            try {
                repository.getToken().flowOn(coroutineDispatcher).collect() { tokenAndId ->
                    setValueOnMainThread(LoginState.Success(token = tokenAndId[0], id = tokenAndId[1]))
                }
            } catch (e: Exception) {
                setValueOnMainThread(LoginState.Failure(error = "No token found"))
            }
        }
    }

    // Function to generate a Toast
    fun mToast(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}