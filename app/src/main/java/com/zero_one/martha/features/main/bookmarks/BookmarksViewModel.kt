package com.zero_one.martha.features.main.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zero_one.martha.data.domain.repository.UserRepository
import com.zero_one.martha.data.source.datastore.user.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val userManager: UserManager,
    private val userRepository: UserRepository
): ViewModel() {
    private val _bookmarks: MutableStateFlow<MutableMap<String, MutableList<UInt>>?> =
        MutableStateFlow(null)
    val bookmarks = _bookmarks.asStateFlow()

    init {
        viewModelScope.launch {
            _bookmarks.update {
                userManager.getUser().savedBooks
            }
        }
    }

    fun addNewFolder(folderName: String): Boolean {
        if (!_bookmarks.value!!.containsKey(folderName)) {
            _bookmarks.value!![folderName] = mutableListOf()
            _bookmarks.update {
                _bookmarks.value
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
