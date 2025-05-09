package com.zero_one.martha.data.source.network.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: UInt = 0u,
    val email: String = "",
    val username: String = "",
    val image: String = ""
)
