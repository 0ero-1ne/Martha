package com.zero_one.martha.ui.fields.email

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zero_one.martha.R
import com.zero_one.martha.modifier.clearFocusOnKeyboardDismiss

@Composable
fun EmailField(
    modifier: Modifier,
    imeAction: ImeAction,
    state: EmailFieldState = rememberEmailFieldState()
) {
    OutlinedTextField(
        value = state.value,
        onValueChange = state::onValueChanged,
        modifier = modifier
            .clearFocusOnKeyboardDismiss()
            .padding(
                bottom = 5.dp,
            ),
        isError = state.error != null,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email field icon",
            )
        },
        trailingIcon = {
            if (state.value.isNotEmpty())
                IconButton(
                    onClick = state::clear,
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear email field",
                    )
                }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
        ),
        shape = RoundedCornerShape(16.dp),
        placeholder = {
            Text(stringResource(R.string.email_placeholder))
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            errorBorderColor = Color(0xFFC80000),
        ),
    )
    Text(
        text = state.error ?: "",
        maxLines = 2,
        modifier = Modifier
            .padding(
                start = 16.dp,
                bottom = 24.dp,
            ),
        color = Color(0xFFC80000),
        style = MaterialTheme.typography.labelMedium,
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
