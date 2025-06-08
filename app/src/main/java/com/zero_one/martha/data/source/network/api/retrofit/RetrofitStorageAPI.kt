package com.zero_one.martha.data.source.network.api.retrofit

import com.zero_one.martha.data.source.network.api.StorageAPI
import com.zero_one.martha.data.source.network.models.StorageResult
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface RetrofitStorageAPI: StorageAPI {
    @POST(value = "api/images")
    @Multipart
    override suspend fun uploadImage(@Part file: MultipartBody.Part): Response<StorageResult>

    @DELETE(value = "api/images/{uuid}")
    override suspend fun deleteImage(@Path("uuid") uuid: String): Response<StorageResult>
}
