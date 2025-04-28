package com.zero_one.martha.data.source.network.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CommentRate(
    @SerializedName("comment_id")
    val commentId: UInt = 0u,
    @SerializedName("user_id")
    val userId: UInt = 0u,
    val rating: Boolean = false,
    val user: User = User()
)
