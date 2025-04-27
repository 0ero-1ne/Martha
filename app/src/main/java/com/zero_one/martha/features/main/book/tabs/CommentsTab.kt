package com.zero_one.martha.features.main.book.tabs

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    commentEvents: Flow<BookViewModel.CommentValidationEvent>
) {
    val formState = rememberCommentFormState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        commentEvents.collect {event ->
            when (event) {
                is BookViewModel.CommentValidationEvent.Success -> {
                    formState.comment.clear()
                    Log.d("Comments tab", "Comment added")
                }

                is BookViewModel.CommentValidationEvent.Error -> {
                    Log.e("Comments tab", "Comment not added")
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
                    keyboardController?.hide()
                    onSaveComment(formState.comment.value.trim())
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

    comments.forEach {comment ->
        Text(comment.userId.toString())
        Text(comment.text)
        Text("Upvotes = " + comment.rates?.count {it.rating})
        Text("Downvotes = " + comment.rates?.count {!it.rating})
        HorizontalDivider()
    }
}
