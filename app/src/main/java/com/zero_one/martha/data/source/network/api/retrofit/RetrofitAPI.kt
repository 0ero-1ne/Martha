package com.zero_one.martha.data.source.network.api.retrofit

import com.zero_one.martha.data.source.network.api.NetworkAPI
import com.zero_one.martha.data.source.network.models.Book
import com.zero_one.martha.data.source.network.models.Chapter
import com.zero_one.martha.data.source.network.models.Comment
import com.zero_one.martha.data.source.network.models.User
import com.zero_one.martha.data.source.network.models.auth.AuthTokens
import com.zero_one.martha.data.source.network.models.auth.AuthUser
import com.zero_one.martha.data.source.network.models.auth.UpdateToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RetrofitAPI: NetworkAPI {
    // Auth
    @POST(value = "auth/signup")
    override suspend fun signup(@Body authUser: AuthUser): Response<String?>

    @POST(value = "auth/login")
    override suspend fun login(@Body authUser: AuthUser): Response<AuthTokens?>

    @POST(value = "auth/refresh")
    override suspend fun refresh(@Body refreshToken: UpdateToken): Response<AuthTokens?>

    // User
    @GET(value = "users/single")
    override suspend fun getUser(): Response<User?>

    @POST(value = "users")
    override suspend fun updateUser(@Body user: User): Response<User?>

    // Book
    @GET(value = "books")
    override suspend fun getBooks(): Response<List<Book>>

    @GET(value = "books/{id}?withTags=true&withAuthors=true&withComments=true")
    override suspend fun getBookById(@Path("id") id: UInt): Response<Book>

    @GET(value = "books/{id}?withChapters=true")
    override suspend fun getBookForReader(@Path("id") id: UInt): Response<Book>

    // Chapter
    @GET(value = "chapters/book/{id}")
    override suspend fun getChaptersByBookId(@Path("id") bookId: UInt): Response<List<Chapter>>

    @GET(value = "chapters/{id}")
    override suspend fun getChapterById(@Path("id") chapterId: UInt): Response<Chapter>

    // Comment
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
}
