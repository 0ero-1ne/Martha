package com.zero_one.martha.features.main.bookmarks.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zero_one.martha.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    currentTab: Int,
    pages: Set<String>,
    onDismiss: () -> Unit,
    onAddNewFolder: (String) -> Boolean,
    onDeleteFolder: (String) -> Boolean,
    onScrollToFirstTab: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var inputValue by remember {mutableStateOf("")}
    var errorValue by remember {mutableStateOf("")}
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    ModalBottomSheet(
        modifier = Modifier
            .wrapContentSize(),
        sheetState = sheetState,
        onDismissRequest = {
            keyboardController?.hide()
            onDismiss()
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                ),
        ) {
            Text(
                text = stringResource(R.string.managing_folders),
                style = MaterialTheme.typography.titleLarge,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        top = 16.dp,
                    ),
            ) {
                CustomTextField(
                    modifier = Modifier
                        .weight(5f),
                    value = inputValue,
                    onValueChange = {
                        inputValue = it
                        errorValue = ""
                    },
                )
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add new bookmarks folder",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            val value = inputValue.trim()
                            if (value.isNotEmpty()) {
                                val result = onAddNewFolder(value)
                                if (result) {
                                    inputValue = ""
                                    keyboardController?.hide()
                                } else {
                                    errorValue =
                                        context.resources.getString(R.string.error_invalid_folder_name)
                                }
                            } else {
                                errorValue =
                                    context.resources.getString(R.string.error_empty_folder_name)
                            }
                        },
                )
            }
            AnimatedVisibility(errorValue.isNotEmpty()) {
                Text(
                    text = errorValue,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(
                            top = 5.dp,
                        ),
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 16.dp,
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(pages.toList().filter {!isPredefinedFolder(it)}, key = {it}) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(it)
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Remove bookmarks folder",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(0.dp)
                                .size(24.dp)
                                .clickable {
                                    scope.launch {
                                        if (currentTab == pages.indexOf(it)) {
                                            onScrollToFirstTab()
                                        }
                                        if (currentTab == pages.indexOf(pages.last())) {
                                            onScrollToFirstTab()
                                        }
                                        delay(100)
                                        onDeleteFolder(it)
                                    }
                                },
                        )
                    }
                }
            }
        }
    }
}

private fun isPredefinedFolder(folderName: String): Boolean {
    return when (folderName.lowercase()) {
        "ended", "favorites", "planed", "reading", "stopped" -> true
        else -> false
    }
}
