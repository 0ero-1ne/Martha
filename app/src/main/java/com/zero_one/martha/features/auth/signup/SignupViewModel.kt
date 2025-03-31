package com.zero_one.martha.features.auth.signup

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
class SignupViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    private val signupValidationEventChannel = Channel<SignupValidationEvent>()
    val signupValidationEvents = signupValidationEventChannel.receiveAsFlow()

    var signupErrorMessage by mutableStateOf("")
    var isSignupButtonPressed by mutableStateOf(false)

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            isSignupButtonPressed = true
            val signupResult = repository.signup(
                AuthUser(
                    email = email,
                    password = password,
                ),
            )

            isSignupButtonPressed = false
            if (signupResult != null) {
                signupErrorMessage = signupResult
                signupValidationEventChannel.send(SignupValidationEvent.Error)
            } else {
                signupValidationEventChannel.send(SignupValidationEvent.Success)
            }
        }
    }

    sealed class SignupValidationEvent {
        data object Success: SignupValidationEvent()
        data object Error: SignupValidationEvent()
    }
}
