package com.zero_one.martha.data.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
data class Book(
    val id: UInt = 0u,
    val uuid: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    val status: String = "",
    val year: Int = 0,
    val views: Int = 0,
    val cover: String = "",
    val tags: List<Tag> = emptyList(),
    val authors: List<Author> = emptyList(),
    val comments: List<Comment> = emptyList(),
    val chapters: List<Chapter> = emptyList()
)
