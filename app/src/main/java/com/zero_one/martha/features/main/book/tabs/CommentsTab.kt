package com.zero_one.martha.features.main.book.tabs

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.zero_one.martha.data.domain.model.Comment
import com.zero_one.martha.features.main.book.BookViewModel
import com.zero_one.martha.features.main.book.ui.CommentFormModal
import com.zero_one.martha.features.main.book.ui.CommentItem
import com.zero_one.martha.ui.components.NotAuthDialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

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
    userRole: String,
    onNavigateToLoginPage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        var showCommentForm by remember {mutableStateOf(false)}
        var notAuthDialogState by remember {mutableStateOf(false)}
        var editId by remember {mutableStateOf(0u)}
        var editText by remember {mutableStateOf("")}
        var parentId by remember {mutableStateOf(0u)}
        val listState = rememberLazyListState()
        val firstCommentVisibility by remember {
            derivedStateOf {listState.firstVisibleItemIndex}
        }
        val scope = rememberCoroutineScope()

        if (comments == null) {
            CircularProgressIndicator()
            return
        }

        if (comments.isEmpty()) {
            Text(
                text = "No comments...",
                modifier = Modifier
                    .padding(top = 64.dp),
            )
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

        AnimatedVisibility(
            visible = firstCommentVisibility > 0,
            modifier = Modifier
                .clip(CircleShape)
                .zIndex(2f)
                .align(Alignment.BottomCenter)
                .clickable {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background, CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .zIndex(2f)
                    .padding(10.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = "Scroll to first comment",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.End,
        ) {
            item {
                TextButton(
                    onClick = {
                        if (isAuth()) {
                            editId = 0u
                            editText = ""
                            parentId = 0u
                            showCommentForm = true
                            Log.d("Leave a comment", "Clicked")
                        } else {
                            notAuthDialogState = true
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.Comment,
                        contentDescription = "Create comment",
                        modifier = Modifier.padding(end = 10.dp),
                    )
                    Text("Leave comment")
                }
            }
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
                    onNavigateToLoginPage = onNavigateToLoginPage,
                    isAuth = isAuth,
                )
            }
        }

        if (notAuthDialogState) {
            NotAuthDialog(
                onDismiss = {
                    notAuthDialogState = false
                },
                onNavigateToLoginPage = onNavigateToLoginPage,
            )
        }
    }
}
