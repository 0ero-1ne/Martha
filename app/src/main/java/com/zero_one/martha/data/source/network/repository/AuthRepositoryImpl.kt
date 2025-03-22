package com.zero_one.martha.data.source.network.repository

import com.zero_one.martha.data.domain.repository.AuthRepository
import com.zero_one.martha.data.source.network.api.NetworkAPI
import com.zero_one.martha.data.source.network.models.auth.AuthTokens
import com.zero_one.martha.data.source.network.models.auth.AuthUser
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: NetworkAPI
) : AuthRepository {
    override suspend fun login(authUser: AuthUser): AuthTokens {
        TODO("Not yet implemented")
    }

    override suspend fun signup(authUser: AuthUser): AuthTokens {
        TODO("Not yet implemented")
    }

}
