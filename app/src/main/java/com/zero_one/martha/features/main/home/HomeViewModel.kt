package com.zero_one.martha.features.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zero_one.martha.data.domain.model.Book
import com.zero_one.martha.data.domain.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookRepository: BookRepository
): ViewModel() {
    private val _popularBooks = MutableStateFlow<List<Book>?>(null)
    val popularBooks = _popularBooks.asStateFlow()

    private val _newBooks = MutableStateFlow<List<Book>?>(null)
    val newBooks = _newBooks.asStateFlow()

    private val _forYouBooks = MutableStateFlow<List<Book>?>(null)
    val forYouBooks = _forYouBooks.asStateFlow()

    init {
        initPopularBooks()
        initNewBooks()
        initForYouBooks()
    }

    private fun initPopularBooks() {
        viewModelScope.launch {
            _popularBooks.update {
                bookRepository.getBooks()
            }
        }
    }

    private fun initNewBooks() {
        viewModelScope.launch {
            _newBooks.update {
                bookRepository.getBooks()
            }
        }
    }

    private fun initForYouBooks() {
        viewModelScope.launch {
            _forYouBooks.update {
                bookRepository.getBooks()
            }
        }
    }
}
