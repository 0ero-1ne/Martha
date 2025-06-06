package com.zero_one.martha.features.main.book.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.zero_one.martha.data.domain.model.Comment
import com.zero_one.martha.features.main.book.BookViewModel
import com.zero_one.martha.features.main.book.ui.CommentFormModal
import com.zero_one.martha.features.main.bookmarks.components.CommentItem
import kotlinx.coroutines.flow.Flow

@Composable
fun CommentsTab(
    comments: List<Comment>?,
    onSaveComment: (String, UInt) -> Unit,
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
    var parentId by remember {mutableStateOf(0u)}

    TextButton(
        onClick = {
            editId = 0u
            editText = ""
            parentId = 0u
            showCommentForm = true
        },
    ) {
        Text("Leave comment")
    }

    if (showCommentForm) {
        CommentFormModal(
            editId = editId,
            editText = editText,
            parentId = parentId,
            commentEvents = commentEvents,
            onDismiss = {
                editId = 0u
                editText = ""
                parentId = 0u
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

    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.End,
    ) {
        items(comments, key = {it.uuid}) {comment ->
            CommentItem(
                comment = comment,
                onDeleteComment = {id ->
                    onDeleteComment(id)
                },
                onUpdateComment = {id, text, parent ->
                    editId = id
                    editText = text
                    parentId = parent
                    showCommentForm = true
                },
                onRateComment = onRateComment,
                userId = userId,
                userRole = userRole,
                onReplyComment = {id ->
                    parentId = id
                    showCommentForm = true
                },
            )
        }
    }
}
