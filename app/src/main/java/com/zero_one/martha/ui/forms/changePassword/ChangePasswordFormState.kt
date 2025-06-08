package com.zero_one.martha.ui.forms.changePassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.zero_one.martha.ui.fields.password.PasswordFieldState
import com.zero_one.martha.ui.fields.password.rememberPasswordFieldState

@Stable
class ChangePasswordFormState(
    val oldPassword: PasswordFieldState,
    val newPassword: PasswordFieldState
) {
    val isValid: Boolean by derivedStateOf {
        oldPassword.loginValidate() && newPassword.signupValidate()
    }

    fun validate() {
        oldPassword.loginValidate()
        newPassword.signupValidate()
    }
}

@Composable
fun rememberChangePasswordFormState(): ChangePasswordFormState {
    val oldPassword = rememberPasswordFieldState()
    val newPassword = rememberPasswordFieldState()

    return remember {
        ChangePasswordFormState(oldPassword, newPassword)
    }
}
