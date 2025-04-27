package com.zero_one.martha.ui.fields.email

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.zero_one.martha.modifier.clearFocusOnKeyboardDismiss

@Composable
fun EmailField(
    modifier: Modifier,
    imeAction: ImeAction,
    state: EmailFieldState = rememberEmailFieldState()
) {
    TextField(
        value = state.value,
        onValueChange = state::onValueChanged,
        modifier = modifier
            .fillMaxWidth()
            .clearFocusOnKeyboardDismiss(),
        leadingIcon = {
            Icon(Icons.Default.Email, "Email field icon")
        },
        isError = state.error != null,
        supportingText = state.error?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red,
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
        ),
    )
}

@Composable
@Preview
fun EmailFieldPreview(
    state: EmailFieldState = rememberEmailFieldState()
) {
    EmailField(
        state = state,
        imeAction = ImeAction.Done,
        modifier = Modifier,
    )
}
