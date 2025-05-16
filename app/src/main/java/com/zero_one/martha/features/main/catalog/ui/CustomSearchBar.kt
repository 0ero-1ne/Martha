package com.zero_one.martha.features.main.catalog.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun CustomSearchBar(
    onSearch: (query: String) -> Unit
) {
    var query by remember {mutableStateOf("")}
    val interactionSource = remember {MutableInteractionSource()}
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .padding(bottom = 10.dp, top = 10.dp)
            .fillMaxWidth(),
    ) {
        BasicTextField(
            modifier = Modifier
                .height(42.dp)
                .fillMaxWidth()
                .clearFocusOnKeyboardDismiss(),
            value = query,
            singleLine = true,
            interactionSource = interactionSource,
            onValueChange = {value ->
                query = value
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    onSearch(query.trim())
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
                value = query,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Outlined.Search, "Search icon")
                },
                trailingIcon = {
                    if (query.isNotBlank())
                        IconButton(
                            onClick = {
                                query = ""
                                onSearch(query.trim())
                            },
                        ) {
                            Icon(Icons.Outlined.Close, "Clear query icon")
                        }
                },
                placeholder = {Text("Search the book")},
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
