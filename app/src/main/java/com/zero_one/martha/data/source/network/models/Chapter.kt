package com.zero_one.martha.data.source.network.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Chapter(
    val id: UInt = 0u,
    val title: String = "",
    val serial: Int = 0,
    @SerializedName("book_id")
    val bookId: UInt = 0u,
    val text: String = "",
    val audio: String = "",
)
