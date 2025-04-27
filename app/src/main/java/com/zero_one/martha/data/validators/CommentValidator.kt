package com.zero_one.martha.data.validators

fun validateComment(comment: String): CommentValidationResult {
    return when {
        comment.isEmpty() -> CommentValidationResult.EMPTY
        else -> CommentValidationResult.VALID
    }
}

fun isCommentValid(comment: String): Boolean {
    return validateComment(comment) == CommentValidationResult.VALID
}

enum class CommentValidationResult {
    VALID,
    EMPTY,
}
