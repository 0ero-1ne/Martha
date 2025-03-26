package com.zero_one.martha.ui.forms.fields.email

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.zero_one.martha.data.validators.EmailValidationResult
import com.zero_one.martha.data.validators.isEmailValid
import com.zero_one.martha.data.validators.validateEmail

@Stable
class EmailFieldState(initialValue: String) {
    var value: String by mutableStateOf(initialValue)
        private set

    var error: String? by mutableStateOf(null)
        private set

    val isValid: Boolean by derivedStateOf {
        isEmailValid(value)
    }

    fun onValueChanged(email: String) {
        value = email
        error = null
    }

    fun validate() {
        error = when (validateEmail(value)) {
            EmailValidationResult.EMPTY -> "Email is blank"
            EmailValidationResult.INVALID -> "Email is invalid"
            EmailValidationResult.VALID -> null
        }
    }
}

private val emailFieldSaver = listSaver(
    save = {
        listOf(it.value)
    },
    restore = {
        EmailFieldState(it[0])
    },
)

@Composable
fun rememberEmailFieldState(): EmailFieldState {
    return rememberSaveable(
        saver = emailFieldSaver,
    ) {
        EmailFieldState(initialValue = "")
    }
}
