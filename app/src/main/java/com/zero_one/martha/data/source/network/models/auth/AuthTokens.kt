package com.zero_one.martha.data.source.network.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokens(
    val accessToken: String,
    val refreshToken: String
)
