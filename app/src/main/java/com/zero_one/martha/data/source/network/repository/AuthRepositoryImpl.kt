package com.zero_one.martha.data.source.network.repository

import android.util.Log
import com.zero_one.martha.data.domain.repository.AuthRepository
import com.zero_one.martha.data.source.datastore.user.UserManager
import com.zero_one.martha.data.source.datastore.user.tokens.TokensManager
import com.zero_one.martha.data.source.network.api.NetworkAPI
import com.zero_one.martha.data.source.network.models.auth.AuthUser
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: NetworkAPI,
    private val tokensManager: TokensManager,
    private val userManager: UserManager
): AuthRepository {
    override suspend fun login(authUser: AuthUser): String? {
        try {
            val response = api.login(authUser)

            if (response.isSuccessful && response.body() != null) {
                tokensManager.setTokens(response.body()!!)
                return null
            }

            val errorMessage = response.errorBody()?.string()
            return errorMessage?.substring(1, errorMessage.length - 1)
        } catch (err: Exception) {
            Log.e("AuthRepositoryImpl", "Login method", err)
            return "No response, try again later"
        }
    }

    override suspend fun signup(authUser: AuthUser): String? {
        try {
            val response = api.signup(authUser)

            if (response.isSuccessful && response.body() != null) {
                login(
                    AuthUser(
                        email = authUser.email,
                        password = authUser.password,
                    ),
                )
                return null
            }

            val errorMessage = response.errorBody()?.string()
            return errorMessage?.substring(1, errorMessage.length - 1)
        } catch (err: Exception) {
            Log.e("AuthRepositoryImpl", "Signup method", err)
            return "No response, try again later"
        }
    }

    override suspend fun logout() {
        tokensManager.removeTokens()
        userManager.removeUser()
    }
}
