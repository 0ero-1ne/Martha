package com.zero_one.martha.ui.fields.comment

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.zero_one.martha.modifier.clearFocusOnKeyboardDismiss

@Composable
fun CommentField(
    modifier: Modifier,
    state: CommentFieldState = rememberCommentFieldState()
) {
    TextField(
        value = state.value,
        onValueChange = state::onValueChanged,
        modifier = modifier
            .clearFocusOnKeyboardDismiss(),
        isError = state.error != null,
        supportingText = state.error?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red,
                )
            }
        },
        maxLines = 4,
    )
}
