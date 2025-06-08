package com.zero_one.martha.ui.fields.username

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.zero_one.martha.data.validators.UsernameValidationResult
import com.zero_one.martha.data.validators.isUsernameValid
import com.zero_one.martha.data.validators.validateUsername

@Stable
class UsernameFieldState(initialValue: String) {
    var value: String by mutableStateOf(initialValue)
        private set

    var error: String? by mutableStateOf(null)

    val isValid: Boolean by derivedStateOf {
        isUsernameValid(value)
    }

    fun onValueChanged(username: String) {
        value = username
        error = null
    }

    fun validate(): Boolean {
        error = when (validateUsername(value)) {
            UsernameValidationResult.VALID -> null
            UsernameValidationResult.INVALID_LENGTH -> "Username length must be 6 or more"
            UsernameValidationResult.INVALID -> "Username must contain only letters, digits, \"-\" and \"_\""
            UsernameValidationResult.EMPTY -> "Username is blank"
        }

        return error == null
    }

    fun clear() {
        value = ""
        error = null
    }
}

private val usernameFieldSaver = listSaver(
    save = {
        listOf(it.value)
    },
    restore = {
        UsernameFieldState(it[0])
    },
)

@Composable
fun rememberUsernameFieldState(): UsernameFieldState {
    return rememberSaveable(
        saver = usernameFieldSaver,
    ) {
        UsernameFieldState(initialValue = "")
    }
}
