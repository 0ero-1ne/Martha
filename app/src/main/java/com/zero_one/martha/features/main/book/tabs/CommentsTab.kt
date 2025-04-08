package com.zero_one.martha.features.main.book.tabs

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.zero_one.martha.data.domain.model.Comment

@Composable
fun CommentsTab(
    comments: List<Comment>
) {
    if (comments.isEmpty()) {
        Text("No comments...")
        return
    }

    comments.forEach {comment ->
        Text(comment.userId.toString())
        Text(comment.text)
        HorizontalDivider()
    }
}
