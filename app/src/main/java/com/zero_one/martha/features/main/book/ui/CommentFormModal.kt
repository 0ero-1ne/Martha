package com.zero_one.martha.features.main.book.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zero_one.martha.R
import com.zero_one.martha.features.main.book.BookViewModel
import com.zero_one.martha.ui.forms.comment.CommentForm
import com.zero_one.martha.ui.forms.comment.rememberCommentFormState
import com.zero_one.martha.utils.parseCommentFieldError
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentFormModal(
    editId: UInt = 0u,
    editText: String = "",
    parentId: UInt = 0u,
    commentEvents: Flow<BookViewModel.CommentValidationEvent>,
    onDismiss: () -> Unit,
    onSaveComment: (String, UInt) -> Unit,
    onUpdateComment: (UInt, String) -> Unit,
    isAuth: () -> Boolean
) {
    val modalState = rememberModalBottomSheetState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val commentFormState = rememberCommentFormState()
    val context = LocalContext.current
    commentFormState.comment.onValueChanged(editText)

    LaunchedEffect(key1 = context) {
        commentEvents.collect {event ->
            when (event) {
                is BookViewModel.CommentValidationEvent.Success -> {
                    commentFormState.comment.clear()
                    onDismiss()
                    Log.d("Comments tab", "Comment success")
                }

                is BookViewModel.CommentValidationEvent.Error -> {
                    Log.e("Comments tab", "Comment failed")
                }
            }
        }
    }

    ModalBottomSheet(
        sheetState = modalState,
        onDismissRequest = onDismiss,
        modifier = Modifier
            .wrapContentHeight(),
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                ),
        ) {
            CommentForm(
                state = commentFormState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
            )
            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(56.dp)
                    .fillMaxWidth(),
                enabled = isAuth(),
                onClick = {
                    commentFormState.validate()

                    if (commentFormState.isValid) {
                        if (isAuth()) {
                            keyboardController?.hide()
                            if (editId != 0u) {
                                onUpdateComment(editId, commentFormState.comment.value.trim())
                            } else {
                                onSaveComment(commentFormState.comment.value.trim(), parentId)
                            }
                        }
                    } else {
                        commentFormState.comment.error = parseCommentFieldError(
                            commentFormState.comment.error,
                            context,
                        )
                    }
                },
                shape = RoundedCornerShape(5.dp),
            ) {
                var text = stringResource(R.string.not_authorized)
                if (isAuth()) {
                    text = if (editId == 0u)
                        stringResource(R.string.leave_comment)
                    else stringResource(R.string.update_comment)
                }
                Text(
                    text = if (parentId == 0u)
                        text
                    else stringResource(R.string.reply_comment),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}
