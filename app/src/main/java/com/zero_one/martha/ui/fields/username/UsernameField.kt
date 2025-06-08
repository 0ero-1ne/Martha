package com.zero_one.martha.ui.fields.username

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zero_one.martha.modifier.clearFocusOnKeyboardDismiss

@Composable
fun UsernameField(
    modifier: Modifier,
    imeAction: ImeAction,
    state: UsernameFieldState = rememberUsernameFieldState()
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
                imageVector = Icons.Default.Person,
                contentDescription = "Username field icon",
            )
        },
        trailingIcon = {
            if (state.value.isNotEmpty())
                IconButton(
                    onClick = state::clear,
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear username field",
                    )
                }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
        ),
        shape = RoundedCornerShape(16.dp),
        placeholder = {
            Text("Enter the username")
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            errorBorderColor = Color(0xFFC80000),
        ),
    )
    Text(
        text = state.error ?: "",
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
fun UsernameFieldPreview(
    state: UsernameFieldState = rememberUsernameFieldState()
) {
    UsernameField(
        state = state,
        imeAction = ImeAction.Done,
        modifier = Modifier,
    )
}
