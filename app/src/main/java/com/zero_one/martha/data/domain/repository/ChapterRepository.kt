package com.zero_one.martha.data.domain.repository

import com.zero_one.martha.data.domain.model.Chapter

interface ChapterRepository {
    suspend fun getChaptersByBookId(bookId: UInt): List<Chapter>
    suspend fun getChapterById(chapterId: UInt): Chapter
}
