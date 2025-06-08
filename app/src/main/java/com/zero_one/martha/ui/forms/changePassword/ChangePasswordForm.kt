package com.zero_one.martha.ui.forms.changePassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.zero_one.martha.ui.fields.password.PasswordField

@Composable
fun ChangePasswordForm(
    state: ChangePasswordFormState = rememberChangePasswordFormState()
) {
    Column {
        PasswordField(
            modifier = Modifier
                .fillMaxWidth(),
            imeAction = ImeAction.Done,
            state = state.oldPassword,
        )
        PasswordField(
            modifier = Modifier
                .fillMaxWidth(),
            imeAction = ImeAction.Done,
            state = state.newPassword,
        )
    }
}
