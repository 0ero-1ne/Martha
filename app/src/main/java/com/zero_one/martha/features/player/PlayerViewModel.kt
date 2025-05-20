package com.zero_one.martha.features.player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.zero_one.martha.data.domain.model.Book
import com.zero_one.martha.data.domain.model.Chapter
import com.zero_one.martha.data.domain.model.SavedBook
import com.zero_one.martha.data.domain.repository.BookRepository
import com.zero_one.martha.data.domain.repository.ChapterRepository
import com.zero_one.martha.data.domain.repository.UserRepository
import com.zero_one.martha.data.source.datastore.user.UserManager
import com.zero_one.martha.features.main.book.UIntNavType
import com.zero_one.martha.features.reader.ReaderRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository,
    private val chapterRepository: ChapterRepository,
    private val userManager: UserManager,
    private val userRepository: UserRepository
): ViewModel() {
    var book: Book? by mutableStateOf(null)
    var currentChapter: Chapter? by mutableStateOf(null)

    private var savedBook = SavedBook()
    private var folderName = ""
    var timeState by mutableLongStateOf(-1L)

    init {
        viewModelScope.launch {
            val params = savedStateHandle.toRoute<ReaderRoute>(
                typeMap = mapOf(
                    typeOf<UInt>() to UIntNavType,
                ),
            )

            book = bookRepository.getBookForReader(params.bookId)

            currentChapter = if (book!!.chapters.isNotEmpty() && params.chapterId == 0u) {
                chapterRepository.getChapterById(book!!.chapters.minBy {it.id}.id)
            } else {
                chapterRepository.getChapterById(params.chapterId)
            }

            userManager.getUser().savedBooks.forEach {(key, savedBooks) ->
                val hasBookmark =
                    savedBooks.filter {it.bookId == book!!.id}
                if (hasBookmark.isNotEmpty()) {
                    savedBook = savedBooks.first {it.bookId == book!!.id}
                    folderName = key
                }
            }

            if (currentChapter!!.id == savedBook.audioChapter) {
                timeState = savedBook.audio
            } else {
                timeState = 0L
            }
        }
    }

    private fun saveCurrentPosition(timeState: Long) {
        viewModelScope.launch {
            val bookmarks = userManager.getUser().savedBooks.toMutableMap()

            if (currentChapter!!.id < savedBook.audioChapter) {
                return@launch
            }

            bookmarks[folderName]?.remove(savedBook)

            savedBook = savedBook.copy(
                bookId = book!!.id,
                audioChapter = currentChapter!!.id,
                audio = timeState,
            )

            bookmarks["Reading"]!!.add(savedBook)

            userManager.setUser(
                userManager.getUser().copy(
                    savedBooks = bookmarks.toMap(),
                ),
            )

            userRepository.updateUser(userManager.getUser())
        }
    }

    fun destroy(timeState: Long) {
        saveCurrentPosition(timeState)
    }
}
