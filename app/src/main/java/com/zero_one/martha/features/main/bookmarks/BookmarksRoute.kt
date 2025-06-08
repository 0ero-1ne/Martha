package com.zero_one.martha.features.main.bookmarks

import kotlinx.serialization.Serializable

@Serializable
data class BookmarksRoute(
    val folderName: String = ""
)
