package com.zero_one.martha.features.main.catalog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zero_one.martha.data.domain.model.Book
import com.zero_one.martha.data.domain.model.Filters
import com.zero_one.martha.data.domain.model.Tag
import com.zero_one.martha.data.domain.repository.BookRepository
import com.zero_one.martha.data.domain.repository.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val tagRepository: TagRepository,
): ViewModel() {
    private val _books: MutableStateFlow<List<Book>?> = MutableStateFlow(null)
    val books = _books.asStateFlow()

    private val _tags: MutableStateFlow<List<Tag>> = MutableStateFlow(listOf())
    val tags = _tags.asStateFlow()

    private val _tagFilters: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val tagFilters = _tagFilters.asStateFlow()

    var searching by mutableStateOf(false)

    var columns: Int by mutableIntStateOf(3)

    init {
        viewModelScope.launch {
            _books.update {
                bookRepository.getBooks()
            }
            _tags.update {
                tagRepository.getTags()
            }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            searching = true
            val filters = Filters(
                tags = _tagFilters.value,
            )
            val booksList = bookRepository.getBooksByQuery(
                query = query.lowercase(),
                filters = filters,
            )
            _books.update {booksList}
            searching = false
        }
    }

    fun onAddTagFilter(tag: String) {
        _tagFilters.update {
            val list = it.toMutableList()
            if (!list.contains(tag)) {
                list.add(tag)
            }
            list.toList()
        }
    }

    fun onRemoveTagFilter(tag: String) {
        _tagFilters.update {
            val list = it.toMutableList()
            if (list.contains(tag)) {
                list.remove(tag)
            }
            list.toList()
        }
    }
}
