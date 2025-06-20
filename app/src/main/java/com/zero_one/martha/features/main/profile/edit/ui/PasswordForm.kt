package com.zero_one.martha.features.main.profile.edit.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zero_one.martha.R
import com.zero_one.martha.ui.forms.changePassword.ChangePasswordForm
import com.zero_one.martha.ui.forms.changePassword.ChangePasswordFormState
import com.zero_one.martha.utils.parsePasswordFieldError

@Composable
fun PasswordForm(
    changePasswordFormState: ChangePasswordFormState,
    isLoading: Boolean,
    onChangePassword: (oldPassword: String, newPassword: String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    ChangePasswordForm(
        state = changePasswordFormState,
    )

    Button(
        enabled = !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = {
            if (isLoading) {
                return@Button
            }

            keyboardController?.hide()
            changePasswordFormState.validate()

            if (changePasswordFormState.isValid) {
                onChangePassword(
                    changePasswordFormState.oldPassword.value,
                    changePasswordFormState.newPassword.value,
                )
            } else {
                changePasswordFormState.oldPassword.error = parsePasswordFieldError(
                    changePasswordFormState.oldPassword.error,
                    context,
                )
                changePasswordFormState.newPassword.error = parsePasswordFieldError(
                    changePasswordFormState.newPassword.error,
                    context,
                )
            }
        },
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.background,
            )
        } else {
            Text(
                text = stringResource(R.string.profile_edit_save_password_button),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
