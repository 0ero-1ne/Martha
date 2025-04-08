package com.zero_one.martha.data.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val id: UInt = 0u,
    val title: String = ""
)
