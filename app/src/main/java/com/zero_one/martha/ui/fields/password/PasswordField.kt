package com.zero_one.martha.ui.fields.password

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.zero_one.martha.R
import com.zero_one.martha.modifier.clearFocusOnKeyboardDismiss

@Composable
fun PasswordField(
    modifier: Modifier,
    imeAction: ImeAction,
    state: PasswordFieldState = rememberPasswordFieldState()
) {
    var isPasswordVisible by remember {mutableStateOf(false)}

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
            IconButton(onClick = {isPasswordVisible = !isPasswordVisible}) {
                Icon(
                    imageVector = if (isPasswordVisible) {
                        Icons.Default.Visibility
                    } else {
                        Icons.Default.VisibilityOff
                    },
                    contentDescription = null,
                )
            }
        },
        trailingIcon = {
            if (state.value.isNotEmpty())
                IconButton(
                    onClick = state::clear,
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear password field",
                    )
                }
        },
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
        ),
        shape = RoundedCornerShape(16.dp),
        placeholder = {
            Text(stringResource(R.string.password_placeholder))
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
