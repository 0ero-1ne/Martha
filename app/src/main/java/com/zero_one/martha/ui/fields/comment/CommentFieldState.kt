package com.zero_one.martha.ui.fields.comment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.zero_one.martha.data.validators.CommentValidationResult
import com.zero_one.martha.data.validators.isCommentValid
import com.zero_one.martha.data.validators.validateComment

@Stable
class CommentFieldState(initialValue: String) {
    var value: String by mutableStateOf(initialValue)
        private set

    var error: String? by mutableStateOf(null)

    val isValid: Boolean by derivedStateOf {
        isCommentValid(value)
    }

    fun onValueChanged(comment: String) {
        value = comment
        error = null
    }

    fun validate() {
        error = when (validateComment(value)) {
            CommentValidationResult.EMPTY -> "empty"
            CommentValidationResult.VALID -> null
        }
    }

    fun clear() {
        error = null
        value = ""
    }
}

private val commentFieldSaver = listSaver(
    save = {
        listOf(it.value)
    },
    restore = {
        CommentFieldState(it[0])
    },
)

@Composable
fun rememberCommentFieldState(): CommentFieldState {
    return rememberSaveable(
        saver = commentFieldSaver,
    ) {
        CommentFieldState(initialValue = "")
    }
}
