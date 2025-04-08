package com.zero_one.martha.data.source.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: UInt = 0u,
    val text: String = "",
    val userId: UInt = 0u,
)
