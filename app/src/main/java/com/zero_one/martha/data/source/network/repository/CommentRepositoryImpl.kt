package com.zero_one.martha.data.source.network.repository

import android.util.Log
import com.zero_one.martha.data.domain.model.Comment
import com.zero_one.martha.data.domain.model.CommentRate
import com.zero_one.martha.data.domain.model.User
import com.zero_one.martha.data.domain.repository.CommentRepository
import com.zero_one.martha.data.source.network.api.NetworkAPI
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val api: NetworkAPI
): CommentRepository {
    override suspend fun getCommentsByBookId(bookId: UInt): List<Comment> {
        try {
            val commentsResult = api.getCommentsByBookId(bookId)

            if (commentsResult.isSuccessful && commentsResult.body() != null) {
                return commentsResult.body()!!.map {
                    networkCommentToComment(it)
                }
            }

            return emptyList()
        } catch (e: Exception) {
            Log.e("CommentRepositoryImpl", "getCommentsByBookId", e)
            return emptyList()
        }
    }

    override suspend fun saveComment(comment: Comment): Comment {
        try {
            Log.d(
                "Comment data",
                "Text = ${comment.text} UserId = ${comment.userId} BookId = ${comment.bookId}",
            )
            val commentResult = api.saveComment(commentToNetworkComment(comment))

            if (commentResult.isSuccessful && commentResult.body() != null) {
                return networkCommentToComment(commentResult.body()!!)
            }

            return Comment()
        } catch (e: Exception) {
            Log.e("CommentRepositoryImpl", "saveComment", e)
            return Comment()
        }
    }

    override suspend fun updateComment(comment: Comment): Comment {
        try {
            val commentResult = api.updateComment(commentToNetworkComment(comment), comment.id)

            if (commentResult.isSuccessful && commentResult.body() != null) {
                return networkCommentToComment(commentResult.body()!!)
            }

            return Comment()
        } catch (e: Exception) {
            Log.e("CommentRepositoryImpl", "updateComment", e)
            return Comment()
        }
    }

    override suspend fun deleteComment(commentId: UInt): Boolean {
        try {
            val commentResult = api.deleteComment(commentId)
            return commentResult.isSuccessful
        } catch (e: Exception) {
            Log.e("CommentRepositoryImpl", "deleteComment", e)
            return false
        }
    }

    private fun networkCommentToComment(comment: com.zero_one.martha.data.source.network.models.Comment): Comment {
        return Comment(
            id = comment.id,
            bookId = comment.bookId,
            userId = comment.userId,
            text = comment.text,
            rates = comment.rates.map {networkCommentRateToCommentRate(it)},
            user = User(
                id = comment.user.id,
                username = comment.user.username,
                email = comment.user.email,
                image = comment.user.image,
            ),
        )
    }

    private fun commentToNetworkComment(comment: Comment): com.zero_one.martha.data.source.network.models.Comment {
        return com.zero_one.martha.data.source.network.models.Comment(
            id = comment.id,
            bookId = comment.bookId,
            userId = comment.userId,
            text = comment.text,
            rates = comment.rates.map {commentRateToNetwork(it)},
            user = com.zero_one.martha.data.source.network.models.User(
                id = comment.user.id,
                username = comment.user.username,
                email = comment.user.email,
                image = comment.user.image,
            ),
        )
    }

    private fun networkCommentRateToCommentRate(commentRate: com.zero_one.martha.data.source.network.models.CommentRate): CommentRate {
        return CommentRate(
            commentId = commentRate.commentId,
            userId = commentRate.userId,
            rating = commentRate.rating,
            user = User(
                id = commentRate.user.id,
                username = commentRate.user.username,
                email = commentRate.user.email,
                image = commentRate.user.image,
            ),
        )
    }

    private fun commentRateToNetwork(commentRate: CommentRate): com.zero_one.martha.data.source.network.models.CommentRate {
        return com.zero_one.martha.data.source.network.models.CommentRate(
            commentId = commentRate.commentId,
            userId = commentRate.userId,
            rating = commentRate.rating,
            user = com.zero_one.martha.data.source.network.models.User(
                id = commentRate.user.id,
                username = commentRate.user.username,
                email = commentRate.user.email,
                image = commentRate.user.image,
            ),
        )
    }
}
