package com.zero_one.martha.ui.forms.comment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.zero_one.martha.ui.fields.comment.CommentFieldState
import com.zero_one.martha.ui.fields.comment.rememberCommentFieldState

@Stable
class CommentFormState(
    val comment: CommentFieldState
) {
    val isValid: Boolean by derivedStateOf {
        comment.isValid
    }

    fun validate() {
        comment.validate()
    }
}

@Composable
fun rememberCommentFormState(): CommentFormState {
    val comment = rememberCommentFieldState()

    return remember {
        CommentFormState(comment)
    }
}
