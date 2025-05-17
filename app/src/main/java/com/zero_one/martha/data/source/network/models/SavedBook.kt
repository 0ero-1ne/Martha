package com.zero_one.martha.data.source.network.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SavedBook(
    @SerializedName("book_id")
    val bookId: UInt = 0u,
    @SerializedName("reader_chapter")
    val readerChapter: UInt = 0u,
    val page: Int = 0,
    @SerializedName("audio_chapter")
    val audioChapter: UInt = 0u,
    val audio: Long = 0L
)
