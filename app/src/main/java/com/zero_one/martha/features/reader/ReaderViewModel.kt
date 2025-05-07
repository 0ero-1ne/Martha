package com.zero_one.martha.features.reader

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
import com.zero_one.martha.data.domain.repository.BookRepository
import com.zero_one.martha.data.domain.repository.ChapterRepository
import com.zero_one.martha.features.main.book.UIntNavType
import com.zero_one.martha.utils.getFileBufferedReader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
): ViewModel() {
    var book: Book? by mutableStateOf(null)
    var currentChapter: Chapter? by mutableStateOf(null)

    private val _reader: MutableStateFlow<String?> = MutableStateFlow(null)
    val reader = _reader.asStateFlow()

    var bufferedReader: BufferedReader? by mutableStateOf(null)

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    val url = "https://filesamples.com/samples/document/txt/sample3.txt"

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
            loadFile()
        }
    }

    fun nextPage() {
        // if (currentPage < _reader.value!!.size - 1) {
        //     currentPage++
        // }
    }

    fun prevPage() {
        // if (currentPage > 0) {
        //     currentPage--
        // }
    }

    fun loadFile() {
        GlobalScope.launch {
            _isLoading.value = true
            bufferedReader = getFileBufferedReader(url)
            _isLoading.value = false
        }
    }

    fun loadPages() {
        GlobalScope.launch {
            _reader.value = bufferedReader!!.readText()
        }
    }

    fun destroy() {
        bufferedReader?.close()
    }
}
