package com.zero_one.martha.features.main.bookmarks.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.zero_one.martha.modifier.clearFocusOnKeyboardDismiss

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    onAddNewFolder: (String) -> Boolean,
) {
    val sheetState = rememberModalBottomSheetState()
    var inputValue by remember {mutableStateOf("")}
    var errorValue by remember {mutableStateOf("")}

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            OutlinedTextField(
                value = inputValue,
                isError = errorValue.isNotEmpty(),
                supportingText = {
                    Text(errorValue)
                },
                onValueChange = {
                    inputValue = it
                    errorValue = ""
                },
                modifier = Modifier.clearFocusOnKeyboardDismiss(),
            )
            Button(
                onClick = {
                    val value = inputValue.trim()
                    if (value.isNotEmpty()) {
                        val result = onAddNewFolder(value)
                        if (result) {
                            inputValue = ""
                            keyboardController?.hide()
                        } else {
                            errorValue = "Tab already exists"
                        }
                    }
                },
            ) {
                Text("Add folder")
            }
        }
    }
}
