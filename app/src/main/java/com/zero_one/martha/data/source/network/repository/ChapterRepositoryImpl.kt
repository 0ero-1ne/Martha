package com.zero_one.martha.data.source.network.repository

import android.util.Log
import com.zero_one.martha.data.domain.model.Chapter
import com.zero_one.martha.data.domain.repository.ChapterRepository
import com.zero_one.martha.data.source.network.api.NetworkAPI
import javax.inject.Inject

class ChapterRepositoryImpl @Inject constructor(
    private val api: NetworkAPI
): ChapterRepository {
    override suspend fun getChaptersByBookId(bookId: UInt): List<Chapter> {
        try {
            val chaptersResult = api.getChaptersByBookId(bookId)

            if (chaptersResult.isSuccessful && chaptersResult.body() != null) {
                return chaptersResult.body()!!.map {chapter ->
                    networkChapterToChapter(chapter)
                }
            }

            return emptyList()
        } catch (e: Exception) {
            Log.e("ChapterRepositoryImpl", "getChaptersByBookId()", e)
            return emptyList()
        }
    }

    override suspend fun getChapterById(chapterId: UInt): Chapter {
        try {
            val chapterResult = api.getChapterById(chapterId)

            if (chapterResult.isSuccessful && chapterResult.body() != null) {
                return networkChapterToChapter(chapterResult.body()!!)
            }

            return Chapter()
        } catch (e: Exception) {
            Log.e("ChapterRepositoryImpl", "getChapterById()", e)
            return Chapter()
        }
    }

    private fun networkChapterToChapter(chapter: com.zero_one.martha.data.source.network.models.Chapter): Chapter {
        return Chapter(
            id = chapter.id,
            title = chapter.title,
            serial = chapter.serial,
            text = chapter.text,
            audio = chapter.audio,
            bookId = chapter.bookId,
        )
    }
}
