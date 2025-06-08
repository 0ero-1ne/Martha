package com.zero_one.martha.data.source.network.repository

import android.util.Log
import com.zero_one.martha.data.domain.repository.StorageRepository
import com.zero_one.martha.data.source.network.api.StorageAPI
import com.zero_one.martha.data.source.network.models.StorageResult
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storageAPI: StorageAPI
): StorageRepository {
    override suspend fun uploadImage(file: File): StorageResult {
        try {
            val uploadImageResult = storageAPI.uploadImage(
                file = MultipartBody.Part.createFormData(
                    name = "file",
                    filename = file.name,
                    body = file.asRequestBody(),
                ),
            )

            if (uploadImageResult.isSuccessful && uploadImageResult.body() != null) {
                return StorageResult(
                    data = uploadImageResult.body()!!.data,
                )
            }

            return StorageResult(
                error = uploadImageResult.body()!!.error,
            )
        } catch (e: Exception) {
            Log.e("StorageRepositoryImpl", "uploadImage", e)
            return StorageResult(
                error = "Server error, try again later",
            )
        }
    }

    override suspend fun deleteImage(uuid: String): StorageResult {
        try {
            val deleteImageResult = storageAPI.deleteImage(uuid)

            if (deleteImageResult.isSuccessful && deleteImageResult.body() != null) {
                return StorageResult()
            }

            return StorageResult(
                error = deleteImageResult.body()!!.error,
            )
        } catch (e: Exception) {
            Log.e("StorageRepositoryImpl", "deleteImage", e)
            return StorageResult(
                error = "Server error, try again later",
            )
        }
    }
}
