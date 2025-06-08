package com.zero_one.martha.data.domain.repository

import com.zero_one.martha.data.domain.model.User
import com.zero_one.martha.data.source.network.repository.UserRepositoryImpl

interface UserRepository {
    suspend fun getUser(): User?
    suspend fun updateUser(user: User): UserRepositoryImpl.UpdateUserResult
}
