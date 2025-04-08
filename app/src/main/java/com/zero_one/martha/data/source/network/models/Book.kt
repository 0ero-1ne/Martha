package com.zero_one.martha.data.source.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: UInt = 0u,
    val title: String = "",
    val description: String = "",
    val status: String = "",
    val year: Int = 0,
    val views: Int = 0,
    val cover: String = "",
    val tags: List<Tag>? = null,
    val authors: List<Author>? = null,
    val comments: List<Comment>? = null
)
