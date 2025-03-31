package com.zero_one.martha.data.source.network.models.auth

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateToken(
    @SerializedName("refresh_token")
    val refreshToken: String
)
