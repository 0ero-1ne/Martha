package com.zero_one.martha.features.main.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun BookmarkItem(
    onNavigateToBookmarks: (String) -> Unit,
    savedBook: String,
    savedBookSize: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                MaterialTheme.colorScheme.surfaceContainer,
                RoundedCornerShape(16.dp),
            )
            .clickable {
                onNavigateToBookmarks(savedBook)
            },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = folderIcon(savedBook),
                contentDescription = "Bookmark folder",
                modifier = Modifier
                    .padding(bottom = 6.dp)
                    .size(36.dp),
            )
            Text(
                text = savedBookSize,
            )
            Text(
                text = savedBook,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxSize(),
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

private fun folderIcon(folderName: String): ImageVector {
    return when (folderName) {
        "Ended" -> Icons.Default.Bookmark
        "Reading" -> Icons.Default.Visibility
        "Favorites" -> Icons.Default.Favorite
        "Planed" -> Icons.Default.CalendarMonth
        "Stopped" -> Icons.Default.Close
        else -> Icons.Default.Book
    }
}
