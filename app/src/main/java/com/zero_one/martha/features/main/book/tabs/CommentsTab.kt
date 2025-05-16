package com.zero_one.martha.features.main.book.tabs

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.zero_one.martha.data.domain.model.Comment
import com.zero_one.martha.features.main.book.BookViewModel
import com.zero_one.martha.ui.forms.comment.CommentForm
import com.zero_one.martha.ui.forms.comment.rememberCommentFormState
import kotlinx.coroutines.flow.Flow

@Composable
fun CommentsTab(
    comments: List<Comment>?,
    onSaveComment: (String) -> Unit,
    onUpdateComment: (UInt, String) -> Unit,
    onDeleteComment: (UInt) -> Unit,
    commentEvents: Flow<BookViewModel.CommentValidationEvent>,
    isAuth: () -> Boolean,
    userId: UInt,
) {
    val formState = rememberCommentFormState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    var commentId by remember {mutableStateOf(0u)}

    LaunchedEffect(key1 = context) {
        commentEvents.collect {event ->
            when (event) {
                is BookViewModel.CommentValidationEvent.Success -> {
                    formState.comment.clear()
                    commentId = 0u
                    Log.d("Comments tab", "Comment success")
                }

                is BookViewModel.CommentValidationEvent.Error -> {
                    Log.e("Comments tab", "Comment failed")
                }
            }
        }
    }

    Row {
        CommentForm(formState)
        Button(
            onClick = {
                formState.validate()

                if (formState.isValid) {
                    if (isAuth()) {
                        keyboardController?.hide()
                        if (commentId != 0u) {
                            onUpdateComment(commentId, formState.comment.value.trim())
                        } else {
                            onSaveComment(formState.comment.value.trim())
                        }
                    }
                }
            },
        ) {
            Text("Save")
        }
    }

    if (comments == null) {
        CircularProgressIndicator()
        return
    }

    if (comments.isEmpty()) {
        Text("No comments...")
        return
    }

    LazyColumn {
        items(comments, key = {it.uuid}) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(),
            ) {
                Text(it.userId.toString())
                Text(it.user.username)
                Text(it.text)
                Text("Upvotes = " + it.rates.count {it.rating})
                Text("Downvotes = " + it.rates.count {!it.rating})
                if (userId == it.userId) {
                    Button(
                        onClick = {
                            onDeleteComment(it.id)
                        },
                    ) {
                        Icon(Icons.Filled.Delete, "Delete comment")
                    }
                    Button(
                        onClick = {
                            formState.comment.onValueChanged(it.text)
                            commentId = it.id
                        },
                    ) {
                        Icon(Icons.Default.Edit, "Edit comment")
                    }
                }
                HorizontalDivider()
            }
        }
    }
}
