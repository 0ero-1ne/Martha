package com.zero_one.martha.features.main.book.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Headphones
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zero_one.martha.data.domain.model.Chapter

@Composable
fun ChaptersListRow(
    chapter: Chapter,
    onNavigateToReader: (bookId: UInt, chapterId: UInt) -> Unit,
    onNavigateToPlayer: (bookId: UInt, chapterId: UInt) -> Unit,
    onNavigateToLoginPage: () -> Unit,
    isAuth: () -> Boolean
) {
    val dialogState = remember {mutableStateOf(false)}

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
                    onClick = {
                        if (isAuth())
                            onNavigateToReader(chapter.bookId, chapter.id)
                        else {
                            dialogState.value = true
                        }
                    },
                ) {
                    Icon(Icons.Outlined.Book, "Read chapter icon")
                }
            }

            if (chapter.audio != "") {
                IconButton(
                    onClick = {
                        if (isAuth())
                            onNavigateToPlayer(chapter.bookId, chapter.id)
                        else
                            dialogState.value = true
                    },
                ) {
                    Icon(Icons.Outlined.Headphones, "Listen chapter icon")
                }
            }
        }

        if (dialogState.value)
            DialogIfNotAuth(
                state = dialogState,
                onNavigateToLoginPage = onNavigateToLoginPage,
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogIfNotAuth(
    state: MutableState<Boolean>,
    onNavigateToLoginPage: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = {
            state.value = false
        },
    ) {
        Surface {
            Column {
                Text("Oops, you can't do it now")
                Text("You need to login to continue the surface")
                Row {
                    TextButton(
                        onClick = {
                            onNavigateToLoginPage()
                            state.value = false
                        },
                    ) {
                        Text("Go to login")
                    }
                    TextButton(
                        onClick = {
                            state.value = false
                        },
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        }
    }
}
