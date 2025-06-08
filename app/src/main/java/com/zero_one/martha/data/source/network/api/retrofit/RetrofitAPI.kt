package com.zero_one.martha.data.source.network.api.retrofit

import com.zero_one.martha.data.source.network.api.NetworkAPI
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
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitAPI: NetworkAPI {
    // Auth
    @POST(value = "auth/signup")
    override suspend fun signup(@Body authUser: AuthUser): Response<String?>

    @POST(value = "auth/login")
    override suspend fun login(@Body authUser: AuthUser): Response<AuthTokens?>

    @POST(value = "auth/refresh")
    override suspend fun refresh(@Body refreshToken: UpdateToken): Response<AuthTokens?>

    @POST(value = "auth/change_password")
    override suspend fun changePassword(@Body changePassword: ChangePassword): Response<String>

    // User
    @GET(value = "users/single")
    override suspend fun getUser(): Response<User?>

    @POST(value = "users")
    override suspend fun updateUser(@Body user: User): Response<User?>

    // Book
    @GET(value = "books")
    override suspend fun getBooks(): Response<List<Book>>

    @GET(value = "books/{id}?withTags=true&withAuthors=true&withComments=true&withBookRates=true")
    override suspend fun getBookById(@Path("id") id: UInt): Response<Book>

    @GET(value = "books/{id}?withChapters=true")
    override suspend fun getBookForReader(@Path("id") id: UInt): Response<Book>

    @GET(value = "books")
    override suspend fun getBooksByQuery(
        @Query("query") query: String,
        @Query("tags") tags: String
    ): Response<List<Book>>

    // Chapter
    @GET(value = "chapters/book/{id}")
    override suspend fun getChaptersByBookId(@Path("id") bookId: UInt): Response<List<Chapter>>

    @GET(value = "chapters/{id}")
    override suspend fun getChapterById(@Path("id") chapterId: UInt): Response<Chapter>

    // Comment
    @GET(value = "comments")
    override suspend fun getUserComments(@Query("user_id") userId: UInt): Response<List<Comment>>

    @GET(value = "comments/book/{id}")
    override suspend fun getCommentsByBookId(@Path("id") bookId: UInt): Response<List<Comment>>

    @POST(value = "comments")
    override suspend fun saveComment(@Body comment: Comment): Response<Comment>

    @PUT(value = "comments/{id}")
    override suspend fun updateComment(
        @Body comment: Comment,
        @Path("id") id: UInt
    ): Response<Comment>

    @DELETE(value = "comments/{id}")
    override suspend fun deleteComment(@Path("id") commentId: UInt): Response<String>

    // Comment Rate
    @POST(value = "comment_rates")
    override suspend fun createCommentRate(@Body commentRate: CommentRate): Response<CommentRate>

    @PUT(value = "comment_rates")
    override suspend fun updateCommentRate(@Body commentRate: CommentRate): Response<CommentRate>

    @DELETE(value = "comment_rates/{commentId}/{userId}")
    override suspend fun deleteCommentRate(
        @Path("commentId") commentId: UInt,
        @Path("userId") userId: UInt
    ): Response<String>

    // Book Rate
    @POST(value = "book_rates")
    override suspend fun createBookRate(@Body bookRate: BookRate): Response<BookRate>

    @PUT(value = "book_rates")
    override suspend fun updateBookRate(@Body bookRate: BookRate): Response<BookRate>

    @DELETE(value = "book_rates/{bookId}/{userId}")
    override suspend fun deleteBookRate(
        @Path("bookId") bookId: UInt,
        @Path("userId") userId: UInt
    ): Response<Boolean>

    // Tag
    @GET(value = "tags")
    override suspend fun getTags(): Response<List<Tag>>
}
