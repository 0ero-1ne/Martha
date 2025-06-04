package com.zero_one.martha.features.main.book.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.zero_one.martha.R
import com.zero_one.martha.data.domain.model.Book
import com.zero_one.martha.data.domain.model.Chapter
import com.zero_one.martha.data.domain.model.SavedBook

@Composable
fun BookHeader(
    book: Book,
    folderName: String,
    onOpenBottomSheet: () -> Unit,
    isAuth: () -> Boolean,
    onNavigateToReader: (bookId: UInt, chapterId: UInt) -> Unit,
    onNavigateToPlayer: (bookId: UInt, chapterId: UInt) -> Unit,
    savedBook: SavedBook,
    chapters: List<Chapter>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .height(200.dp)
                .padding(
                    bottom = 16.dp,
                ),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(book.cover)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Book ${book.id} cover",
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.ic_no_cover),
                    modifier = Modifier
                        .height(200.dp)
                        .width(150.dp)
                        .clip(shape = RoundedCornerShape(10.dp)),
                )
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
            ) {
                Text(
                    text = book.title,
                    maxLines = 3,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(bottom = 5.dp),
                )
                FlowRow {
                    book.authors.forEach {author ->
                        Text(
                            modifier = Modifier.padding(end = 10.dp),
                            text = author.fullname,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
                OutlinedButton(
                    enabled = isAuth(),
                    onClick = onOpenBottomSheet,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    modifier = Modifier.padding(
                        top = 16.dp,
                    ),
                ) {
                    val text = if (!isAuth()) "Not auth" else folderName
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Bookmark icon",
                        modifier = Modifier
                            .padding(end = 5.dp),
                    )
                    Text(
                        text = text.ifEmpty {"Save in ..."},
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedButton(
                enabled = isAuth() && chapters.isNotEmpty(),
                onClick = {
                    onNavigateToReader(book.id, savedBook.readerChapter)
                },
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .weight(1f),
            ) {
                val text = if (!isAuth())
                    "Not auth"
                else {
                    if (savedBook.readerChapter == 0u)
                        "Start"
                    else "Continue"
                }

                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = "Book icon",
                    modifier = Modifier
                        .padding(end = 5.dp),
                )
                if (chapters.isNotEmpty())
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                    )
            }
            OutlinedButton(
                enabled = isAuth() && chapters.isNotEmpty(),
                onClick = {
                    onNavigateToPlayer(book.id, savedBook.audioChapter)
                },
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .weight(1f),
            ) {
                val text = if (!isAuth())
                    "Not auth"
                else {
                    if (savedBook.audioChapter == 0u)
                        "Start"
                    else "Continue"
                }

                Icon(
                    imageVector = Icons.Default.Headphones,
                    contentDescription = "Book icon",
                    modifier = Modifier
                        .padding(end = 5.dp),
                )
                if (chapters.isNotEmpty())
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                    )
            }
        }
    }
}
