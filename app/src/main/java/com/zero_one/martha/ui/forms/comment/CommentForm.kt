package com.zero_one.martha.ui.forms.comment

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zero_one.martha.ui.fields.comment.CommentField

@Composable
fun CommentForm(
    state: CommentFormState = rememberCommentFormState()
) {
    CommentField(
        modifier = Modifier,
        state = state.comment,
    )
}
