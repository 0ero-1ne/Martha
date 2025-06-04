package com.zero_one.martha.data.source.network.repository

import android.util.Log
import com.zero_one.martha.data.domain.model.CommentRate
import com.zero_one.martha.data.domain.repository.CommentRateRepository
import com.zero_one.martha.data.source.network.api.NetworkAPI
import javax.inject.Inject

class CommentRateRepositoryImpl @Inject constructor(
    private val api: NetworkAPI
): CommentRateRepository {
    override suspend fun createCommentRate(commentRate: CommentRate): CommentRate {
        try {
            val createdCommentRate = api.createCommentRate(commentRateToNetwork(commentRate))

            if (createdCommentRate.isSuccessful && createdCommentRate.body() != null) {
                return networkToCommentRate(createdCommentRate.body()!!)
            }

            return CommentRate()
        } catch (e: Exception) {
            Log.e("CommentRateRepositoryImpl", "createCommentRate", e)
            return CommentRate()
        }
    }

    override suspend fun updateCommentRate(commentRate: CommentRate): CommentRate {
        try {
            val updatedCommentRate = api.updateCommentRate(commentRateToNetwork(commentRate))

            if (updatedCommentRate.isSuccessful && updatedCommentRate.body() != null) {
                return networkToCommentRate(updatedCommentRate.body()!!)
            }

            return CommentRate()
        } catch (e: Exception) {
            Log.e("CommentRateRepositoryImpl", "updateCommentRate", e)
            return CommentRate()
        }
    }

    override suspend fun deleteCommentRate(commentRate: CommentRate): Boolean {
        try {
            val deletedCommentRate = api.deleteCommentRate(
                commentId = commentRate.commentId,
                userId = commentRate.userId,
            )

            return deletedCommentRate.isSuccessful
        } catch (e: Exception) {
            Log.e("CommentRateRepositoryImpl", "deleteCommentRate", e)
            return false
        }
    }

    private fun commentRateToNetwork(commentRate: CommentRate): com.zero_one.martha.data.source.network.models.CommentRate {
        return com.zero_one.martha.data.source.network.models.CommentRate(
            commentId = commentRate.commentId,
            userId = commentRate.userId,
            rating = commentRate.rating,
        )
    }

    private fun networkToCommentRate(commentRate: com.zero_one.martha.data.source.network.models.CommentRate): CommentRate {
        return CommentRate(
            commentId = commentRate.commentId,
            userId = commentRate.userId,
            rating = commentRate.rating,
        )
    }
}
