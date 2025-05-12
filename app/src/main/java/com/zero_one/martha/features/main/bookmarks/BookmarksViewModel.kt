package com.zero_one.martha.features.main.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zero_one.martha.data.domain.repository.UserRepository
import com.zero_one.martha.data.source.datastore.user.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val userManager: UserManager,
    private val userRepository: UserRepository
): ViewModel() {
    val user = userManager.getUserFlow()

    private val _bookmarks: MutableStateFlow<Map<String, List<UInt>>?> =
        MutableStateFlow(null)
    val bookmarks = _bookmarks
        .onStart {init()}
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(1000),
            initialValue = null,
        )

    init {
        init()
    }

    private fun init() {
        viewModelScope.launch {
            _bookmarks.update {
                userManager.getUser().savedBooks
            }
        }
    }

    fun addNewFolder(folderName: String): Boolean {
        if (!_bookmarks.value!!.containsKey(folderName)) {
            _bookmarks.update {
                val newBookmarks = _bookmarks.value!!.toMutableMap()
                newBookmarks[folderName] = mutableListOf()
                newBookmarks
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
}
