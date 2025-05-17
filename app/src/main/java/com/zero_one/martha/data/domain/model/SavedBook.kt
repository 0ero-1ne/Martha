package com.zero_one.martha.data.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SavedBook(
    val bookId: UInt = 0u,
    val readerChapter: UInt = 0u,
    val page: Int = 0,
    val audioChapter: UInt = 0u,
    val audio: Long = 0L
)
