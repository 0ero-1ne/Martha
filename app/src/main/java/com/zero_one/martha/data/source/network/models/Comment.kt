package com.zero_one.martha.data.source.network.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: UInt = 0u,
    @SerializedName("parent_id")
    val parentId: UInt = 0u,
    val text: String = "",
    @SerializedName("user_id")
    val userId: UInt = 0u,
    @SerializedName("book_id")
    val bookId: UInt = 0u,
    val rates: List<CommentRate>? = null,
    val replies: List<Comment>? = null,
    val user: User = User()
)
