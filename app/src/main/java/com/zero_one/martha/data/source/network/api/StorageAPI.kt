package com.zero_one.martha.data.source.network.api

import com.zero_one.martha.data.source.network.models.StorageResult
import okhttp3.MultipartBody
import retrofit2.Response

interface StorageAPI {
    // IMAGE
    suspend fun uploadImage(file: MultipartBody.Part): Response<StorageResult>
    suspend fun deleteImage(uuid: String): Response<StorageResult>
}
