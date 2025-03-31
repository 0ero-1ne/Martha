package com.zero_one.martha.ui.forms.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.zero_one.martha.ui.forms.fields.email.EmailFieldState
import com.zero_one.martha.ui.forms.fields.email.rememberEmailFieldState
import com.zero_one.martha.ui.forms.fields.password.PasswordFieldState
import com.zero_one.martha.ui.forms.fields.password.rememberPasswordFieldState

@Stable
class SignupFormState(
    val email: EmailFieldState,
    val password: PasswordFieldState
) {
    val isValid: Boolean by derivedStateOf {
        email.isValid && password.signupValidate()
    }

    fun validate() {
        email.validate()
        password.loginValidate()
    }
}

@Composable
fun rememberSignupFormState(): SignupFormState {
    val email = rememberEmailFieldState()
    val password = rememberPasswordFieldState()

    return remember {
        SignupFormState(email, password)
    }
}
