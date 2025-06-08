package com.zero_one.martha.features.main.book.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zero_one.martha.utils.parseSystemFolderName

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
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(bookmarks.toList(), key = {it}) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(parseSystemFolderName(it))
                        Icon(
                            imageVector = if (currentFolderName == it) Icons.Rounded.Bookmark
                            else Icons.Default.BookmarkBorder,
                            contentDescription = "Remove bookmarks folder",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(0.dp)
                                .size(24.dp)
                                .clickable {
                                    if (currentFolderName.isEmpty()) {
                                        onSaveBookInBookmarks(it)
                                    } else {
                                        if (currentFolderName == it)
                                            onRemoveBookFromBookmarks(it)
                                        else onReplaceBookmark(currentFolderName, it)
                                    }
                                    onDismiss()
                                },
                        )
                    }
                }
            }
        }
    }
}
