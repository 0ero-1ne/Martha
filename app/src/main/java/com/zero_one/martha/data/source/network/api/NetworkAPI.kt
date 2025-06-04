package com.zero_one.martha.data.source.network.api

import com.zero_one.martha.data.source.network.models.Book
import com.zero_one.martha.data.source.network.models.Chapter
import com.zero_one.martha.data.source.network.models.Comment
import com.zero_one.martha.data.source.network.models.CommentRate
import com.zero_one.martha.data.source.network.models.Tag
import com.zero_one.martha.data.source.network.models.User
import com.zero_one.martha.data.source.network.models.auth.AuthTokens
import com.zero_one.martha.data.source.network.models.auth.AuthUser
import com.zero_one.martha.data.source.network.models.auth.UpdateToken
import retrofit2.Response

interface NetworkAPI {
    // Auth
    suspend fun login(authUser: AuthUser): Response<AuthTokens?>
    suspend fun signup(authUser: AuthUser): Response<String?>
    suspend fun refresh(refreshToken: UpdateToken): Response<AuthTokens?>

    // User
    suspend fun getUser(): Response<User?>
    suspend fun updateUser(user: User): Response<User?>

    // Book
    suspend fun getBooks(): Response<List<Book>>
    suspend fun getBookById(id: UInt): Response<Book>
    suspend fun getBookForReader(id: UInt): Response<Book>
    suspend fun getBooksByQuery(
        query: String,
        tags: String
    ): Response<List<Book>>

    // Chapter
    suspend fun getChaptersByBookId(bookId: UInt): Response<List<Chapter>>
    suspend fun getChapterById(chapterId: UInt): Response<Chapter>

    // Comment
    suspend fun getCommentsByBookId(bookId: UInt): Response<List<Comment>>
    suspend fun saveComment(comment: Comment): Response<Comment>
    suspend fun updateComment(comment: Comment, id: UInt): Response<Comment>
    suspend fun deleteComment(commentId: UInt): Response<String>

    // Comment Rate
    suspend fun createCommentRate(commentRate: CommentRate): Response<CommentRate>
    suspend fun updateCommentRate(commentRate: CommentRate): Response<CommentRate>
    suspend fun deleteCommentRate(commentId: UInt, userId: UInt): Response<String>

    // Tag
    suspend fun getTags(): Response<List<Tag>>
}
