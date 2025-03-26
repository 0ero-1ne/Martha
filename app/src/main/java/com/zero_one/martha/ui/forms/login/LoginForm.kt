package com.zero_one.martha.ui.forms.login

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zero_one.martha.ui.forms.fields.email.EmailField
import com.zero_one.martha.ui.forms.fields.password.PasswordField

@Composable
fun LoginForm(
    state: LoginFormState = rememberLoginFormState()
) {
    Column {
        EmailField(
            modifier = Modifier,
            state = state.email,
        )
        PasswordField(
            modifier = Modifier,
            state = state.password,
        )
    }
}
