package com.zero_one.martha.features.main.book.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetBookmarks(
    onDismiss: () -> Unit,
    bookmarks: Set<String>,
    currentFolderName: String,
    onSaveBookInBookmarks: (String) -> Unit,
    onRemoveBookFromBookmarks: (String) -> Unit,
    onReplaceBookmark: (String, String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
        ) {
            bookmarks.forEach {
                Row(
                    modifier = Modifier
                        .padding(
                            top = 2.dp,
                            bottom = 2.dp,
                        )
                        .clickable {
                            if (currentFolderName.isNotEmpty()) {
                                onReplaceBookmark(currentFolderName, it)
                            } else {
                                onSaveBookInBookmarks(it)
                            }
                            onDismiss()
                        },
                ) {
                    Text(it)
                }
            }
            if (currentFolderName.isNotEmpty()) {
                Button(
                    onClick = {
                        onRemoveBookFromBookmarks(currentFolderName)
                        onDismiss()
                    },
                ) {
                    Text("Remove")
                }
            }
        }
    }
}
