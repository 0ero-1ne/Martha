package com.zero_one.martha.ui.fields.password

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.zero_one.martha.data.validators.PasswordValidationResult
import com.zero_one.martha.data.validators.validatePassword

@Stable
class PasswordFieldState(initialValue: String) {
    var value: String by mutableStateOf(initialValue)
        private set

    var error: String? by mutableStateOf(null)

    fun onValueChanged(password: String) {
        value = password
        error = null
    }

    fun signupValidate(): Boolean {
        error = when (validatePassword(value)) {
            PasswordValidationResult.VALID -> null
            PasswordValidationResult.INVALID_LENGTH -> "invalid_length"
            PasswordValidationResult.INVALID -> "invalid"
            PasswordValidationResult.EMPTY -> "empty"
        }

        return error == null
    }

    fun loginValidate(): Boolean {
        error = when (validatePassword(value)) {
            PasswordValidationResult.VALID -> null
            PasswordValidationResult.EMPTY -> "empty"
            PasswordValidationResult.INVALID_LENGTH -> null
            PasswordValidationResult.INVALID -> null
        }

        return error == null
    }

    fun clear() {
        value = ""
        error = null
    }
}

private val passwordFieldSaver = listSaver(
    save = {
        listOf(it.value)
    },
    restore = {
        PasswordFieldState(it[0])
    },
)

@Composable
fun rememberPasswordFieldState(): PasswordFieldState {
    return rememberSaveable(
        saver = passwordFieldSaver,
    ) {
        PasswordFieldState(initialValue = "")
    }
}
