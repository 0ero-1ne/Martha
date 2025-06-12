package com.zero_one.martha.data.source.network.api

import com.zero_one.martha.data.source.network.models.Book
import com.zero_one.martha.data.source.network.models.BookRate
import com.zero_one.martha.data.source.network.models.Chapter
import com.zero_one.martha.data.source.network.models.Comment
import com.zero_one.martha.data.source.network.models.CommentRate
import com.zero_one.martha.data.source.network.models.Tag
import com.zero_one.martha.data.source.network.models.User
import com.zero_one.martha.data.source.network.models.auth.AuthTokens
import com.zero_one.martha.data.source.network.models.auth.AuthUser
import com.zero_one.martha.data.source.network.models.auth.ChangePassword
import com.zero_one.martha.data.source.network.models.auth.UpdateToken
import retrofit2.Response

interface NetworkAPI {
    // Auth
    suspend fun login(authUser: AuthUser): Response<AuthTokens?>
    suspend fun signup(authUser: AuthUser): Response<String?>
    suspend fun refresh(refreshToken: UpdateToken): Response<AuthTokens?>
    suspend fun changePassword(changePassword: ChangePassword): Response<String>

    // User
    suspend fun getUser(): Response<User?>
    suspend fun updateUser(user: User): Response<User?>

    // Book
    suspend fun getBooks(): Response<List<Book>>
    suspend fun getBookById(id: UInt): Response<Book>
    suspend fun getBookForReader(id: UInt): Response<Book>
    suspend fun getBooksByQuery(
        query: String,
        tags: String,
        statuses: String,
        startYear: Int,
        endYear: Int
    ): Response<List<Book>>

    // Chapter
    suspend fun getChaptersByBookId(bookId: UInt): Response<List<Chapter>>
    suspend fun getChapterById(chapterId: UInt): Response<Chapter>

    // Comment
    suspend fun getUserComments(userId: UInt): Response<List<Comment>>
    suspend fun getCommentsByBookId(bookId: UInt): Response<List<Comment>>
    suspend fun saveComment(comment: Comment): Response<Comment>
    suspend fun updateComment(comment: Comment, id: UInt): Response<Comment>
    suspend fun deleteComment(commentId: UInt): Response<String>

    // Comment Rate
    suspend fun createCommentRate(commentRate: CommentRate): Response<CommentRate>
    suspend fun updateCommentRate(commentRate: CommentRate): Response<CommentRate>
    suspend fun deleteCommentRate(commentId: UInt, userId: UInt): Response<String>

    // Book Rate
    suspend fun createBookRate(bookRate: BookRate): Response<BookRate>
    suspend fun updateBookRate(bookRate: BookRate): Response<BookRate>
    suspend fun deleteBookRate(bookId: UInt, userId: UInt): Response<Boolean>

    // Tag
    suspend fun getTags(): Response<List<Tag>>
}
