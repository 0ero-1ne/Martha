package com.zero_one.martha.data.source.network.models

import kotlinx.serialization.Serializable

@Serializable
data class StorageResult(
    val data: String? = null,
    val error: String? = null
)
