package com.zero_one.martha.features.main.bookmarks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.BasicAlertDialog
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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
    size: Dp,
    onBookClick: (UInt) -> Unit,
    onDeleteBookmark: (SavedBook) -> Unit
) {
    Box(
        modifier = Modifier
            .wrapContentSize(),
    ) {
        Box(
            modifier = Modifier
                .padding(
                    top = 3.dp,
                    end = 3.dp,
                )
                .zIndex(2f)
                .background(MaterialTheme.colorScheme.background, CircleShape)
                .align(Alignment.TopEnd),
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "Remove bookmark",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(3.dp)
                    .align(Alignment.TopEnd)
                    .clickable {
                        onDeleteBookmark(savedBook)
                    }
                    .size(15.dp),
            )
        }
        Column(
            modifier = Modifier
                .padding(
                    bottom = 16.dp,
                )
                .fillMaxWidth(),
        ) {
            if (book == null) {
                CircularProgressIndicator()
                return
            }

            val dialogState = remember {mutableStateOf(false)}

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(book.cover)
                    .crossfade(true)
                    .build(),
                contentDescription = "Book ${book.id} cover",
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.ic_no_cover),
                modifier = Modifier
                    .height(size)
                    .padding(
                        bottom = 5.dp,
                    )
                    .clip(RoundedCornerShape(5.dp))
                    .clickable {
                        onBookClick(book.id)
                    },
            )
            Text(
                text = book.title,
                maxLines = 2,
                style = MaterialTheme.typography.bodyMedium,
            )

            if (dialogState.value) {
                DialogIfBookHasNoChapters(
                    state = dialogState,
                )
            }
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
