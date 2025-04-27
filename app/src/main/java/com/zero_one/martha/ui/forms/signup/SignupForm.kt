package com.zero_one.martha.ui.forms.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.zero_one.martha.ui.fields.email.EmailField
import com.zero_one.martha.ui.fields.password.PasswordField

@Composable
fun SignupForm(
    state: SignupFormState = rememberSignupFormState()
) {
    Column {
        EmailField(
            state = state.email,
            imeAction = ImeAction.Next,
            modifier = Modifier,
        )
        PasswordField(
            state = state.password,
            imeAction = ImeAction.Done,
            modifier = Modifier,
        )
    }
}
