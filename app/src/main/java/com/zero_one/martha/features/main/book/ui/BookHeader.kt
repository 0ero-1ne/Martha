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
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    userRating: Int,
    onOpenBookmarksModal: () -> Unit,
    onOpenRatingModal: () -> Unit,
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    OutlinedButton(
                        enabled = isAuth(),
                        onClick = onOpenBookmarksModal,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(
                            imageVector = if (folderName == "") Icons.Default.BookmarkBorder
                            else Icons.Default.Bookmark,
                            contentDescription = "Bookmark icon",
                        )
                    }
                    OutlinedButton(
                        onClick = onOpenRatingModal,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(
                            imageVector = if (userRating == 0) Icons.Default.StarBorder
                            else Icons.Default.Star,
                            contentDescription = "Bookmark icon",
                        )
                    }
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
