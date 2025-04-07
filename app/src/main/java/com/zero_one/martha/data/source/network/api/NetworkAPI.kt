package com.zero_one.martha.data.source.network.api

import com.zero_one.martha.data.source.network.models.Book
import com.zero_one.martha.data.source.network.models.Chapter
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

    // Book
    suspend fun getBooks(): Response<List<Book>>
    suspend fun getBookById(id: UInt): Response<Book>

    // Chapter
    suspend fun getChaptersByBookId(bookId: UInt): Response<List<Chapter>>
}
