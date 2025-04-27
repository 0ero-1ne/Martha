package com.zero_one.martha.features.reader

import kotlinx.serialization.Serializable

@Serializable
data class ReaderRoute(
    val bookId: UInt,
    val chapterId: UInt,
)
