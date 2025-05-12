package com.zero_one.martha.data.source.network.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: UInt = 0u,
    val email: String = "",
    val username: String = "",
    val image: String = "",
    val role: String = "user",
    @SerializedName("saved_books")
    val savedBooks: Map<String, MutableList<UInt>> = mapOf()
)
