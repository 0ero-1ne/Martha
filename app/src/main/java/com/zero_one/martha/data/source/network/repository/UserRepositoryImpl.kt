package com.zero_one.martha.data.source.network.repository

import android.util.Log
import com.zero_one.martha.data.domain.model.User
import com.zero_one.martha.data.domain.repository.UserRepository
import com.zero_one.martha.data.source.datastore.user.UserManager
import com.zero_one.martha.data.source.datastore.user.tokens.TokensManager
import com.zero_one.martha.data.source.network.api.NetworkAPI
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: NetworkAPI,
    private val tokensManager: TokensManager,
    private val userManager: UserManager
): UserRepository {
    override suspend fun getUser(): User? {
        try {
            if (userManager.hasUser()) {
                return userManager.getUser()
            }

            if (tokensManager.hasTokens()) {
                val userResult = api.getUser()
                if (userResult.isSuccessful && userResult.body() != null) {
                    userManager.setUser(networkUserToUser(userResult.body()!!))
                    return userManager.getUser()
                }
            }

            return null
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "getUserByToken method", e)
            return null
        }
    }

    private fun networkUserToUser(networkUser: com.zero_one.martha.data.source.network.models.User): User {
        return User(
            id = networkUser.id,
            email = networkUser.email,
            username = networkUser.username,
            image = networkUser.image,
        )
    }
}
