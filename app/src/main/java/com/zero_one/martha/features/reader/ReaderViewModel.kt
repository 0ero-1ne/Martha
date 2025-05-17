package com.zero_one.martha.features.reader

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.zero_one.martha.utils.getFileBufferedReader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.BufferedReader
import javax.inject.Inject
import kotlin.reflect.typeOf

@OptIn(DelicateCoroutinesApi::class)
@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository,
    private val chapterRepository: ChapterRepository,
    private val userManager: UserManager,
    private val userRepository: UserRepository
): ViewModel() {
    var book: Book? by mutableStateOf(null)
    private var currentChapter: Chapter? by mutableStateOf(null)

    private val _reader: MutableStateFlow<String?> = MutableStateFlow(null)
    val reader = _reader.asStateFlow()

    var bufferedReader: BufferedReader? by mutableStateOf(null)

    private val _pages: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val pages = _pages.asStateFlow()

    private val url = "https://filesamples.com/samples/document/txt/sample3.txt"

    private var savedBook = SavedBook()
    private var folderName = ""

    var fontSize = mutableIntStateOf(18)
    var maxLines by mutableIntStateOf(0)

    var currentPage by mutableIntStateOf(0)

    init {
        viewModelScope.launch {
            val params = savedStateHandle.toRoute<ReaderRoute>(
                typeMap = mapOf(
                    typeOf<UInt>() to UIntNavType,
                ),
            )

            book = bookRepository.getBookForReader(params.bookId)
            currentChapter = chapterRepository.getChapterById(params.chapterId)
            userManager.getUser().savedBooks.forEach {(key, savedBooks) ->
                val hasBookmark =
                    savedBooks.filter {it.bookId == book!!.id}
                if (hasBookmark.isNotEmpty()) {
                    savedBook = savedBooks.first {it.bookId == book!!.id}
                    folderName = key
                }
            }

            Log.d("Current chapter id", currentChapter!!.id.toString())
            Log.d("Saved chapter id", savedBook.chapterId.toString())

            if (currentChapter!!.id == savedBook.chapterId) {
                currentPage = savedBook.page
            }

            loadFile()
        }
    }

    private fun loadFile() {
        GlobalScope.launch {
            bufferedReader = getFileBufferedReader(url)
        }
    }

    fun loadPages() {
        GlobalScope.launch {
            _reader.value = bufferedReader!!.readText()
        }
    }

    fun addPage(page: String) {
        _pages.update {
            val list = _pages.value.toMutableList()
            list.add(page)
            list.toList()
        }
    }

    fun destroy() {
        viewModelScope.launch {
            bufferedReader?.close()
            val bookmarks = userManager.getUser().savedBooks.toMutableMap()

            if (folderName == "Ended") {
                return@launch
            }

            if (currentChapter!!.id < savedBook.chapterId) {
                return@launch
            }

            bookmarks.values.forEach {list ->
                list.removeIf {it.bookId == book!!.id}
            }

            savedBook = savedBook.copy(
                bookId = book!!.id,
                chapterId = currentChapter!!.id,
                page = currentPage,
            )

            if (currentPage == _pages.value.size - 1 && currentChapter!!.id == book!!.chapters.maxBy {it.id}.id) {
                bookmarks["Ended"]!!.add(savedBook)
            } else {
                bookmarks["Reading"]!!.add(savedBook)
            }

            userManager.setUser(
                userManager.getUser().copy(
                    savedBooks = bookmarks.toMap(),
                ),
            )

            userRepository.updateUser(userManager.getUser())
        }
    }
}
