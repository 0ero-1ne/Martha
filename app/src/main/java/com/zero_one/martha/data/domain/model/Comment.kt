package com.zero_one.martha.data.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Comment(
    val id: UInt = 0u,
    val uuid: String = UUID.randomUUID().toString(),
    val text: String = "",
    @SerializedName("user_id")
    val userId: UInt = 0u,
    @SerializedName("book_id")
    val bookId: UInt = 0u,
    val rates: List<CommentRate> = listOf(),
    val user: User = User()
)
