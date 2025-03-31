package com.zero_one.martha.ui.forms.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zero_one.martha.ui.forms.fields.email.EmailField
import com.zero_one.martha.ui.forms.fields.password.PasswordField

@Composable
fun SignupForm(
    state: SignupFormState = rememberSignupFormState()
) {
    Column {
        EmailField(
            state = state.email,
            modifier = Modifier,
        )
        PasswordField(
            state = state.password,
            modifier = Modifier,
        )
    }
}
