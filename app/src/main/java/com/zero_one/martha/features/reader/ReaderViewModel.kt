package com.zero_one.martha.features.reader

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
import com.zero_one.martha.features.main.book.UIntNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository,
    private val chapterRepository: ChapterRepository,
): ViewModel() {
    var book: Book? by mutableStateOf(null)
    var currentChapter: Chapter? by mutableStateOf(null)

    init {
        viewModelScope.launch {
            val params = savedStateHandle.toRoute<ReaderRoute>(
                typeMap = mapOf(
                    typeOf<UInt>() to UIntNavType,
                ),
            )

            book = bookRepository.getBookForReader(params.bookId)
            currentChapter = chapterRepository.getChapterById(params.chapterId)
        }
    }
}
