package com.zero_one.martha.features.player

import kotlinx.serialization.Serializable

@Serializable
data class PlayerRoute(
    val bookId: UInt,
    val chapterId: UInt,
)
