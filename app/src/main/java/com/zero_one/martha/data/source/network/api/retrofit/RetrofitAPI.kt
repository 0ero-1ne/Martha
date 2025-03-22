package com.zero_one.martha.data.source.network.api.retrofit

import com.zero_one.martha.data.source.network.api.NetworkAPI
import com.zero_one.martha.data.source.network.models.auth.AuthTokens
import com.zero_one.martha.data.source.network.models.auth.AuthUser
import retrofit2.Response
import retrofit2.http.POST

interface RetrofitAPI : NetworkAPI {
    // Auth
    @POST
    override suspend fun signup(authUser: AuthUser): Response<AuthTokens>

    @POST
    override suspend fun login(authUser: AuthUser): Response<AuthTokens>
}
