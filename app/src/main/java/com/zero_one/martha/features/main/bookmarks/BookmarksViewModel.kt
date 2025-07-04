package com.zero_one.martha.features.main.bookmarks

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.zero_one.martha.data.domain.model.Book
import com.zero_one.martha.data.domain.model.SavedBook
import com.zero_one.martha.data.domain.repository.BookRepository
import com.zero_one.martha.data.domain.repository.UserRepository
import com.zero_one.martha.data.source.datastore.user.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userManager: UserManager,
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository
): ViewModel() {
    val user = userManager.getUserFlow()

    private val _books = MutableStateFlow<MutableList<Book>>(mutableListOf())
    val books = _books.asStateFlow()

    private val _bookmarks: MutableStateFlow<Map<String, MutableList<SavedBook>>?> =
        MutableStateFlow(null)
    val bookmarks = _bookmarks
        .onStart {init()}
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    var initFolderName: String = ""

    init {
        init()
        val data = savedStateHandle.toRoute<BookmarksRoute>()
        initFolderName = data.folderName
    }

    private fun init() {
        viewModelScope.launch {
            _bookmarks.update {
                user.first().savedBooks
            }
            _bookmarks.value!!.values.forEach {savedBooks ->
                savedBooks.forEach {
                    loadBook(it.bookId)
                }
            }
        }
    }

    fun addNewFolder(folderName: String): Boolean {
        if (!_bookmarks.value!!.containsKey(folderName)) {
            _bookmarks.update {
                val newBookmarks = _bookmarks.value!!.toMutableMap()
                newBookmarks[folderName] = mutableListOf()
                newBookmarks.toMap()
            }
            updateUserBookmarks()
            return true
        }

        return false
    }

    fun deleteFolder(folderName: String): Boolean {
        if (_bookmarks.value!!.containsKey(folderName)) {
            _bookmarks.update {
                val newBookmarks = _bookmarks.value!!.toMutableMap()
                newBookmarks.remove(folderName)
                newBookmarks
            }
            updateUserBookmarks()
            return true
        }

        return false
    }

    fun onDeleteBookmark(savedBook: SavedBook) {
        viewModelScope.launch {
            val mutable = _bookmarks.value!!.toMutableMap()
            mutable.forEach {(key, value) ->
                if (value.contains(savedBook)) {
                    val newList = value.toMutableList()
                    newList.remove(savedBook)
                    mutable[key] = newList
                }
            }
            Log.d("Mutable", mutable.values.joinToString(","))
            _bookmarks.update {
                mutable.toMap()
            }
            updateUserBookmarks()
        }
    }

    private fun updateUserBookmarks() {
        viewModelScope.launch {
            val user = userManager.getUser()
            userManager.setUser(
                user.copy(
                    savedBooks = _bookmarks.value!!,
                ),
            )
            userRepository.updateUser(userManager.getUser())
        }
    }

    private fun loadBook(bookId: UInt) {
        viewModelScope.launch {
            val book = bookRepository.getBookForReader(bookId)
            if (!_books.value.any {it.id == bookId}) {
                val list = _books.value.toMutableList()
                list.add(book)
                _books.update {list}
            }
        }
    }
}
