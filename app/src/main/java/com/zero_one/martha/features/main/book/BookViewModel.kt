package com.zero_one.martha.features.main.book

import androidx.compose.runtime.getValue
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class BookViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository,
    private val chapterRepository: ChapterRepository
): ViewModel() {
    var book: Book? by mutableStateOf(null)
    var chapters: List<Chapter>? by mutableStateOf(null)

    init {
        viewModelScope.launch {
            val bookId = savedStateHandle.toRoute<BookRoute>(
                typeMap = mapOf(
                    typeOf<UInt>() to BookIdNavType,
                ),
            ).bookId
            book = bookRepository.getBookById(bookId)
            chapters = chapterRepository.getChaptersByBookId(bookId)
        }
    }
}
