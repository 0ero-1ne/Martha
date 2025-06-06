package com.zero_one.martha.data.domain.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Comment(
    val id: UInt = 0u,
    val parentId: UInt = 0u,
    val uuid: String = UUID.randomUUID().toString(),
    val text: String = "",
    val userId: UInt = 0u,
    val bookId: UInt = 0u,
    val rates: List<CommentRate> = listOf(),
    val replies: List<Comment> = listOf(),
    val user: User = User()
)
