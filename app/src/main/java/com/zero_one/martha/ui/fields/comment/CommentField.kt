package com.zero_one.martha.ui.fields.comment

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CommentBank
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zero_one.martha.modifier.clearFocusOnKeyboardDismiss

@Composable
fun CommentField(
    modifier: Modifier,
    state: CommentFieldState = rememberCommentFieldState()
) {
    OutlinedTextField(
        value = state.value,
        onValueChange = state::onValueChanged,
        modifier = modifier
            .clearFocusOnKeyboardDismiss(),
        maxLines = 4,
        placeholder = {
            Text("Leave comment")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.CommentBank,
                contentDescription = "Comment icon",
            )
        },
        trailingIcon = {
            if (state.value.isNotEmpty()) {
                IconButton(
                    onClick = {
                        state.clear()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear comment",
                    )
                }
            }
        },
        shape = RoundedCornerShape(5.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.DarkGray,
            unfocusedBorderColor = Color.DarkGray,
        ),
    )
}
