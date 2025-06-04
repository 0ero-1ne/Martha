package com.zero_one.martha.features.main.book.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.zero_one.martha.data.domain.model.Comment
import com.zero_one.martha.features.main.book.BookViewModel
import com.zero_one.martha.features.main.book.ui.CommentFormModal
import com.zero_one.martha.features.main.bookmarks.components.CommentItem
import kotlinx.coroutines.flow.Flow

@Composable
fun CommentsTab(
    comments: List<Comment>?,
    onSaveComment: (String) -> Unit,
    onUpdateComment: (UInt, String) -> Unit,
    onDeleteComment: (UInt) -> Unit,
    onRateComment: (UInt, Boolean?) -> Unit,
    commentEvents: Flow<BookViewModel.CommentValidationEvent>,
    isAuth: () -> Boolean,
    userId: UInt,
    userRole: String
) {
    var showCommentForm by remember {mutableStateOf(false)}
    var editId by remember {mutableStateOf(0u)}
    var editText by remember {mutableStateOf("")}

    TextButton(
        onClick = {
            editId = 0u
            editText = ""
            showCommentForm = true
        },
    ) {
        Text("Leave comment")
    }

    if (showCommentForm) {
        CommentFormModal(
            editId = editId,
            editText = editText,
            commentEvents = commentEvents,
            onDismiss = {
                showCommentForm = false
            },
            onSaveComment = onSaveComment,
            onUpdateComment = onUpdateComment,
            isAuth = isAuth,
        )
    }

    if (comments == null) {
        CircularProgressIndicator()
        return
    }

    if (comments.isEmpty()) {
        Text("No comments...")
        return
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(comments, key = {it.uuid}) {comment ->
            CommentItem(
                comment = comment,
                onDeleteComment = {
                    onDeleteComment(comment.id)
                },
                onUpdateComment = {
                    editId = comment.id
                    editText = comment.text
                    showCommentForm = true
                },
                onRateComment = onRateComment,
                userId = userId,
                userRole = userRole,
            )
        }
    }
}
