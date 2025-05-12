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
            Log.e("UserRepositoryImpl", "getUser method", e)
            return null
        }
    }

    override suspend fun updateUser(user: User): User? {
        try {
            val updatedUserResult = api.updateUser(userToNetworkUser(user))

            if (updatedUserResult.isSuccessful && updatedUserResult.body() != null) {
                userManager.setUser(networkUserToUser(updatedUserResult.body()!!))
                return userManager.getUser()
            }

            return null
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "updateUser method", e)
            return null
        }
    }

    private fun networkUserToUser(networkUser: com.zero_one.martha.data.source.network.models.User): User {
        val bookmarksMap = mutableMapOf<String, MutableList<UInt>>()
        networkUser.savedBooks.forEach {(key, value) ->
            var listOfIds = value
                .removeSurrounding("[", "]")
                .split(",")
                .map {
                    it.trim().toUIntOrNull() ?: 0u
                }
                .toMutableList()
            if (listOfIds.size == 1 && listOfIds[0] == 0u) {
                listOfIds = mutableListOf()
            }
            bookmarksMap[key] = listOfIds
        }

        return User(
            id = networkUser.id,
            email = networkUser.email,
            username = networkUser.username,
            image = networkUser.image,
            role = networkUser.role,
            savedBooks = bookmarksMap.toMap(),
        )
    }

    private fun userToNetworkUser(user: User): com.zero_one.martha.data.source.network.models.User {
        val bookmarksMap = mutableMapOf<String, String>()
        user.savedBooks.forEach {(key, value) ->
            val stringOfIds =
                if (value.isEmpty() || (value.size == 1 && value[0] == 0u))
                    ""
                else value.joinToString(", ")
            bookmarksMap[key] = "[$stringOfIds]"
        }

        return com.zero_one.martha.data.source.network.models.User(
            id = user.id,
            email = user.email,
            username = user.username,
            image = user.image,
            role = user.role,
            savedBooks = bookmarksMap,
        )
    }
}
