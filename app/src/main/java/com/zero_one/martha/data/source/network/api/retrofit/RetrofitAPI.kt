package com.zero_one.martha.data.source.network.api.retrofit

import com.zero_one.martha.data.source.network.api.NetworkAPI
import com.zero_one.martha.data.source.network.models.Book
import com.zero_one.martha.data.source.network.models.User
import com.zero_one.martha.data.source.network.models.auth.AuthTokens
import com.zero_one.martha.data.source.network.models.auth.AuthUser
import com.zero_one.martha.data.source.network.models.auth.UpdateToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    // Book
    @GET(value = "books/")
    override suspend fun getBooks(): Response<List<Book>>

    @GET(value = "books/{id}")
    override suspend fun getBookById(@Path("id") id: UInt): Response<Book>
}
