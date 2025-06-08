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
import com.zero_one.martha.ui.forms.edit.EditForm
import com.zero_one.martha.ui.forms.edit.EditFormState
import com.zero_one.martha.utils.parseEmailFieldError
import com.zero_one.martha.utils.parseUsernameFieldError

@Composable
fun GeneralForm(
    editFormState: EditFormState,
    isLoading: Boolean,
    onSave: (email: String, username: String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    EditForm(
        state = editFormState,
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
            editFormState.validate()

            if (editFormState.isValid) {
                onSave(
                    editFormState.email.value,
                    editFormState.username.value,
                )
            } else {
                editFormState.username.error = parseUsernameFieldError(
                    editFormState.username.error,
                    context,
                )
                editFormState.email.error = parseEmailFieldError(
                    editFormState.email.error,
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
                text = stringResource(R.string.profile_edit_save_button),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
