package com.zero_one.martha.data.domain.repository

import com.zero_one.martha.data.domain.model.Tag

interface TagRepository {
    suspend fun getTags(): List<Tag>
}
