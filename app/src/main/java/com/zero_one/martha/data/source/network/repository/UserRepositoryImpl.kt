package com.zero_one.martha.data.source.network.repository

import android.util.Log
import com.zero_one.martha.data.domain.model.SavedBook
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
        val savedBooks = mutableMapOf<String, MutableList<SavedBook>>()
        networkUser.savedBooks.forEach {(key, value) ->
            val listOfSavedBooks = value.map {
                networkSavedBookToSavedBook(it)
            }
            savedBooks[key] = listOfSavedBooks.toMutableList()
        }

        return User(
            id = networkUser.id,
            email = networkUser.email,
            username = networkUser.username,
            image = networkUser.image,
            role = networkUser.role,
            savedBooks = savedBooks.toMap(),
        )
    }

    private fun userToNetworkUser(user: User): com.zero_one.martha.data.source.network.models.User {
        val savedBooks =
            mutableMapOf<String, List<com.zero_one.martha.data.source.network.models.SavedBook>>()
        user.savedBooks.forEach {(key, value) ->
            val listOfSavedBooks = value.map {
                savedBookToNetworkSavedBook(it)
            }
            savedBooks[key] = listOfSavedBooks
        }

        return com.zero_one.martha.data.source.network.models.User(
            id = user.id,
            email = user.email,
            username = user.username,
            image = user.image,
            role = user.role,
            savedBooks = savedBooks.toMap(),
        )
    }

    private fun networkSavedBookToSavedBook(network: com.zero_one.martha.data.source.network.models.SavedBook): SavedBook {
        return SavedBook(
            bookId = network.bookId,
            readerChapter = network.readerChapter,
            page = network.page,
            audioChapter = network.audioChapter,
            audio = network.audio,
        )
    }

    private fun savedBookToNetworkSavedBook(savedBook: SavedBook): com.zero_one.martha.data.source.network.models.SavedBook {
        return com.zero_one.martha.data.source.network.models.SavedBook(
            bookId = savedBook.bookId,
            readerChapter = savedBook.readerChapter,
            page = savedBook.page,
            audioChapter = savedBook.audioChapter,
            audio = savedBook.audio,
        )
    }

}
