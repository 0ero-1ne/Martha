package com.zero_one.martha.data.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Chapter(
    val id: UInt = 0u,
    val title: String = "",
    val serial: Int = 0,
    val bookId: UInt = 0u,
    val text: String = "",
    val audio: String = "",
)
