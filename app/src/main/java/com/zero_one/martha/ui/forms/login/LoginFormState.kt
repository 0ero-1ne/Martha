package com.zero_one.martha.ui.forms.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.zero_one.martha.ui.fields.email.EmailFieldState
import com.zero_one.martha.ui.fields.email.rememberEmailFieldState
import com.zero_one.martha.ui.fields.password.PasswordFieldState
import com.zero_one.martha.ui.fields.password.rememberPasswordFieldState

@Stable
class LoginFormState(
    val email: EmailFieldState,
    val password: PasswordFieldState
) {
    val isValid: Boolean by derivedStateOf {
        email.isValid && password.loginValidate()
    }

    fun validate() {
        email.validate()
        password.loginValidate()
    }
}

@Composable
fun rememberLoginFormState(): LoginFormState {
    val email = rememberEmailFieldState()
    val password = rememberPasswordFieldState()

    return remember {
        LoginFormState(email, password)
    }
}

