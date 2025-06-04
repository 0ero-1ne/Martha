package com.zero_one.martha.data.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class BookRate(
    @SerializedName("book_id")
    val bookId: UInt = 0u,
    @SerializedName("user_id")
    val userId: UInt = 0u,
    val rating: Int = 0,
    val user: User = User()
)
