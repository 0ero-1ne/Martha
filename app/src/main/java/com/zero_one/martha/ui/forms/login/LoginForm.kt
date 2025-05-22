package com.zero_one.martha.ui.forms.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.zero_one.martha.ui.fields.email.EmailField
import com.zero_one.martha.ui.fields.password.PasswordField

@Composable
fun LoginForm(
    state: LoginFormState = rememberLoginFormState()
) {
    Column {
        EmailField(
            modifier = Modifier
                .fillMaxWidth(),
            imeAction = ImeAction.Next,
            state = state.email,
        )
        PasswordField(
            modifier = Modifier
                .fillMaxWidth(),
            imeAction = ImeAction.Done,
            state = state.password,
        )
    }
}
