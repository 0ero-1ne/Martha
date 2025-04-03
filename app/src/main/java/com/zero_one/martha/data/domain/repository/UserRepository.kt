package com.zero_one.martha.data.domain.repository

import com.zero_one.martha.data.domain.model.User

interface UserRepository {
    suspend fun getUser(): User?
}
