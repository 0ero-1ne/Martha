package com.zero_one.martha.features.main.bookmarks.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.zero_one.martha.modifier.clearFocusOnKeyboardDismiss

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
) {
    val interactionSource = remember {MutableInteractionSource()}
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier,
    ) {
        BasicTextField(
            modifier = Modifier
                .height(42.dp)
                .fillMaxWidth()
                .clearFocusOnKeyboardDismiss(),
            value = value,
            singleLine = true,
            interactionSource = interactionSource,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                },
            ),
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.primary,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        ) {innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Outlined.Book, "Search icon")
                },
                trailingIcon = {
                    if (value.isNotBlank())
                        IconButton(
                            onClick = {
                                onValueChange("")
                            },
                        ) {
                            Icon(Icons.Outlined.Close, "Clear folder name icon")
                        }
                },
                placeholder = {Text("Enter the folder name")},
                interactionSource = interactionSource,
                visualTransformation = VisualTransformation.None,
                contentPadding = OutlinedTextFieldDefaults.contentPadding(
                    top = 0.dp,
                    bottom = 0.dp,
                ),
                container = {
                    OutlinedTextFieldDefaults.Container(
                        enabled = true,
                        isError = false,
                        interactionSource = interactionSource,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.DarkGray,
                            unfocusedBorderColor = Color.DarkGray,
                        ),
                        shape = RoundedCornerShape(5.dp),
                        focusedBorderThickness = 1.dp,
                        unfocusedBorderThickness = 1.dp,
                    )
                },
            )
        }
    }
}
