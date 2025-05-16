package com.zero_one.martha.features.main.catalog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zero_one.martha.data.domain.model.Book
import com.zero_one.martha.data.domain.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val bookRepository: BookRepository
): ViewModel() {
    private val _books: MutableStateFlow<List<Book>?> = MutableStateFlow(null)
    val books = _books.asStateFlow()
    var searching by mutableStateOf(false)

    var columns: Int by mutableIntStateOf(3)

    init {
        viewModelScope.launch {
            _books.update {
                bookRepository.getBooks()
            }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            searching = true
            val booksList = bookRepository.getBooksByQuery(query.lowercase())
            delay(1000)
            _books.update {booksList}
            searching = false
        }
    }
}
