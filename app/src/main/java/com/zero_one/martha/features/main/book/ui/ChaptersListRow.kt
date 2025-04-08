package com.zero_one.martha.features.main.book.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Headphones
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zero_one.martha.data.domain.model.Chapter

@Composable
fun ChaptersListRow(
    chapter: Chapter
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .width(250.dp),
        ) {
            Text(
                text = "${chapter.serial}. ${chapter.title}",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
        Row {
            if (chapter.text != "") {
                IconButton(
                    onClick = {},
                ) {
                    Icon(Icons.Outlined.Book, "Read chapter icon")
                }
            }

            if (chapter.audio != "") {
                IconButton(
                    onClick = {},
                ) {
                    Icon(Icons.Outlined.Headphones, "Listen chapter icon")
                }
            }
        }
    }
}
