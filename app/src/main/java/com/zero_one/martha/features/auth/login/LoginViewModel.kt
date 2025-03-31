package com.zero_one.martha.features.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zero_one.martha.data.domain.repository.AuthRepository
import com.zero_one.martha.data.source.network.models.auth.AuthUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    private val loginValidationEventChannel = Channel<LoginValidationEvent>()
    val loginValidationEvents = loginValidationEventChannel.receiveAsFlow()

    var loginErrorMessage by mutableStateOf("")
    var isLoginButtonPressed by mutableStateOf(false)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            isLoginButtonPressed = true

            val loginResult = repository.login(
                AuthUser(
                    email = email,
                    password = password,
                ),
            )

            isLoginButtonPressed = false
            if (loginResult != null) {
                loginErrorMessage = loginResult
                loginValidationEventChannel.send(LoginValidationEvent.Error)
            } else {
                loginValidationEventChannel.send(LoginValidationEvent.Success)
            }
        }
    }

    sealed class LoginValidationEvent {
        data object Success: LoginValidationEvent()
        data object Error: LoginValidationEvent()
    }
}
