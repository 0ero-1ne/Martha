package com.zero_one.martha.ui.forms.fields.email

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EmailField(
    modifier: Modifier,
    state: EmailFieldState = rememberEmailFieldState()
) {
    TextField(
        value = state.value,
        onValueChange = state::onValueChanged,
        modifier = modifier
            .fillMaxWidth(),
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
    )
}

@Composable
@Preview
fun EmailFieldPreview(
    state: EmailFieldState = rememberEmailFieldState()
) {
    EmailField(
        state = state,
        modifier = Modifier
    )
}
