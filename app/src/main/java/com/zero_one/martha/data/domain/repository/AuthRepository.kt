package com.zero_one.martha.data.domain.repository

import com.zero_one.martha.data.source.network.models.auth.AuthTokens
import com.zero_one.martha.data.source.network.models.auth.AuthUser

interface AuthRepository {
    suspend fun login(authUser: AuthUser): AuthTokens
    suspend fun signup(authUser: AuthUser): AuthTokens
}
