package com.zero_one.martha.ui.forms.comment

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zero_one.martha.ui.fields.comment.CommentField

@Composable
fun CommentForm(
    modifier: Modifier = Modifier,
    state: CommentFormState = rememberCommentFormState()
) {
    CommentField(
        modifier = modifier
            .fillMaxWidth(),
        state = state.comment,
    )
}
