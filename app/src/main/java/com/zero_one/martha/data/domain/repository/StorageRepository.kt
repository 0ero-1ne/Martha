package com.zero_one.martha.data.domain.repository

import com.zero_one.martha.data.source.network.models.StorageResult
import java.io.File

interface StorageRepository {
    suspend fun uploadImage(file: File): StorageResult
    suspend fun deleteImage(uuid: String): StorageResult
}
