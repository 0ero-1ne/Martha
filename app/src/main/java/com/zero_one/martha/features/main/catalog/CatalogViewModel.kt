package com.zero_one.martha.features.main.catalog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zero_one.martha.data.domain.model.Book
import com.zero_one.martha.data.domain.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val bookRepository: BookRepository
): ViewModel() {
    var books: List<Book>? by mutableStateOf(null)

    init {
        viewModelScope.launch {
            books = bookRepository.getBooks()
        }
    }
}
