package com.zero_one.martha.data.source.network.repository

import android.util.Log
import com.zero_one.martha.data.domain.model.Tag
import com.zero_one.martha.data.domain.repository.TagRepository
import com.zero_one.martha.data.source.network.api.NetworkAPI
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val api: NetworkAPI
): TagRepository {
    override suspend fun getTags(): List<Tag> {
        try {
            val tagsResult = api.getTags()

            if (tagsResult.isSuccessful && tagsResult.body() != null) {
                return tagsResult.body()!!.map {networkTagToTag(it)}
            }

            return emptyList()
        } catch (e: Exception) {
            Log.e("TagRepositoryImpl", "getTags", e)
            return emptyList()
        }
    }

    private fun networkTagToTag(networkTag: com.zero_one.martha.data.source.network.models.Tag): Tag {
        return Tag(
            id = networkTag.id,
            title = networkTag.title,
        )
    }
}
