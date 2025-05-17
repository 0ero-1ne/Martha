package com.zero_one.martha.data.source.network.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SavedBook(
    @SerializedName("book_id")
    val bookId: UInt = 0u,
    @SerializedName("chapter_id")
    val chapterId: UInt = 0u,
    val page: Int = 0,
    val audio: Long = 0L
)
