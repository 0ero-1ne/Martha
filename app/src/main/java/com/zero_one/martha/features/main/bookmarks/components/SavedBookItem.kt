package com.zero_one.martha.features.main.bookmarks.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.zero_one.martha.data.domain.model.SavedBook

@Composable
fun SavedBookItem(
    book: Book?,
    savedBook: SavedBook,
    onNavigateToReader: (bookId: UInt, chapterId: UInt) -> Unit,
    onNavigateToPlayer: (bookId: UInt, chapterId: UInt) -> Unit,
    onBookClick: (UInt) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(
                bottom = 16.dp,
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (book == null) {
            CircularProgressIndicator()
            return
        }

        val dialogState = remember {mutableStateOf(false)}

        Log.d("Saved book read", savedBook.readerChapter.toString())
        Log.d("Saved book audio", savedBook.audioChapter.toString())
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(book.cover)
                .crossfade(true)
                .build(),
            contentDescription = "Book ${book.id} cover",
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.ic_no_cover),
            modifier = Modifier
                .height(100.dp)
                .width(70.dp)
                .clip(RoundedCornerShape(5.dp))
                .clickable {
                    onBookClick(book.id)
                },
        )

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Log.d("Reader state", savedBook.page.toString())
                Button(
                    onClick = {
                        if (book.chapters.isEmpty()) {
                            dialogState.value = true
                            return@Button
                        }
                        onNavigateToReader(savedBook.bookId, savedBook.readerChapter)
                    },
                    modifier = Modifier
                        .padding(end = 5.dp),
                ) {
                    if (book.id == 0u) {
                        CircularProgressIndicator()
                    } else {
                        Icon(
                            imageVector = Icons.Default.Book,
                            contentDescription = "Continue read book",
                        )
                        if (savedBook.readerChapter == 0u) {
                            Text("Start")
                        } else {
                            Text("Continue ${savedBook.readerChapter}")
                        }
                    }
                }

                Log.d("Audio state", savedBook.audio.toString())
                Button(
                    onClick = {
                        if (book.chapters.isEmpty()) {
                            dialogState.value = true
                            return@Button
                        }
                        onNavigateToPlayer(savedBook.bookId, savedBook.audioChapter)
                    },
                ) {
                    if (book.id == 0u) {
                        CircularProgressIndicator()
                    } else {
                        Icon(
                            imageVector = Icons.Default.Headphones,
                            contentDescription = "Continue listen to book",
                        )
                        if (savedBook.audioChapter == 0u) {
                            Text("Start")
                        } else {
                            Text("Continue ${savedBook.audioChapter}")
                        }
                    }
                }
            }
        }

        if (dialogState.value) {
            DialogIfBookHasNoChapters(
                state = dialogState,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogIfBookHasNoChapters(
    state: MutableState<Boolean>,
) {
    BasicAlertDialog(
        onDismissRequest = {
            state.value = false
        },
    ) {
        Surface {
            Column {
                Text("Sorry, but book still has no any chapters")
                Row {
                    TextButton(
                        onClick = {
                            state.value = false
                        },
                    ) {
                        Text("Ok")
                    }
                    TextButton(
                        onClick = {
                            state.value = false
                        },
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}
