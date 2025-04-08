package com.zero_one.martha.data.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
data class Chapter(
    val id: UInt = 0u,
    val uuid: String = UUID.randomUUID().toString(),
    val title: String = "",
    val serial: Int = 0,
    val bookId: UInt = 0u,
    val text: String = "",
    val audio: String = "",
)
