package com.zero_one.martha.data.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SavedBook(
    val bookId: UInt = 0u,
    val chapterId: UInt = 0u,
    val page: Int = 0,
    val audio: Long = 0L
)
