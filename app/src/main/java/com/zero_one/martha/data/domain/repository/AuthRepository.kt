package com.zero_one.martha.data.domain.repository

import com.zero_one.martha.data.source.network.models.auth.AuthUser

interface AuthRepository {
    suspend fun login(authUser: AuthUser): String?
    suspend fun signup(authUser: AuthUser): String?
    suspend fun changePassword(oldPassword: String, newPassword: String): String?
    suspend fun logout()
}
