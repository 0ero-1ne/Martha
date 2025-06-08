package com.zero_one.martha.data.source.network.models.auth

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ChangePassword(
    @SerializedName("old_password")
    val oldPassword: String = "",
    @SerializedName("new_password")
    val newPassword: String = "",
)
