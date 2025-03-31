package com.zero_one.martha.data.source.network.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: UInt,
    val email: String,
    val username: String,
    val image: String
)
