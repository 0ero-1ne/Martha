package com.zero_one.martha.data.source.datastore.user

import androidx.datastore.core.DataStore
import com.zero_one.martha.data.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserManager @Inject constructor(
    private val userDataStore: DataStore<User>
) {
    fun getUserFlow(): Flow<User> {
        return userDataStore.data
    }

    suspend fun getUser(): User {
        return userDataStore.data.first()
    }

    suspend fun hasUser(): Boolean {
        return userDataStore.data.first().id != 0u
    }

    suspend fun setUser(user: User) {
        userDataStore.updateData {
            user
        }
    }

    suspend fun removeUser() {
        userDataStore.updateData {
            User()
        }
    }
}
