package com.zero_one.martha.data.domain.repository

import com.zero_one.martha.data.domain.model.Comment

interface CommentRepository {
    suspend fun getCommentsByBookId(bookId: UInt): List<Comment>
    suspend fun saveComment(comment: Comment): Comment
    suspend fun deleteComment(commentId: UInt): Boolean
}
