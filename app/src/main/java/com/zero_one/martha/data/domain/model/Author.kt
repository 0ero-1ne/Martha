package com.zero_one.martha.data.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Author(
    val id: UInt = 0u,
    val fullname: String = "",
    val biography: String = "",
    val image: String = "",
)
