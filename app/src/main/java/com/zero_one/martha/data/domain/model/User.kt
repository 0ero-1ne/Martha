package com.zero_one.martha.data.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class User(
    val id: UInt = 0u,
    val email: String = "",
    val username: String = "",
    val image: String = "",
    val role: String = "user",
    var savedBooks: Map<String, List<UInt>> = mapOf()
)
