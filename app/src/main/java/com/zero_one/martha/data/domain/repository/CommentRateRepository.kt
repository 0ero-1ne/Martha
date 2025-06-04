package com.zero_one.martha.data.domain.repository

import com.zero_one.martha.data.domain.model.CommentRate

interface CommentRateRepository {
    suspend fun createCommentRate(commentRate: CommentRate): CommentRate
    suspend fun updateCommentRate(commentRate: CommentRate): CommentRate
    suspend fun deleteCommentRate(commentRate: CommentRate): Boolean
}
