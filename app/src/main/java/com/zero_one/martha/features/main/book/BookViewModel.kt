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
import com.zero_one.martha.data.domain.model.Comment
import com.zero_one.martha.data.domain.repository.BookRepository
import com.zero_one.martha.data.domain.repository.ChapterRepository
import com.zero_one.martha.data.domain.repository.CommentRepository
import com.zero_one.martha.data.domain.repository.UserRepository
import com.zero_one.martha.data.source.datastore.user.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class BookViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository,
    private val chapterRepository: ChapterRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val userManager: UserManager
): ViewModel() {
    var user = userManager.getUserFlow()
    var book: Book? by mutableStateOf(null)
    var chapters: List<Chapter>? by mutableStateOf(null)
    var chaptersSortType: Boolean by mutableStateOf(true)
    var comments: List<Comment>? by mutableStateOf(null)
    var bookmarkFolderName by mutableStateOf("")

    private val commentValidationEventChannel = Channel<CommentValidationEvent>()
    val commentValidationEvents = commentValidationEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val bookId = savedStateHandle.toRoute<BookRoute>(
                typeMap = mapOf(
                    typeOf<UInt>() to UIntNavType,
                ),
            ).bookId

            book = bookRepository.getBookById(bookId)
            chapters = chapterRepository.getChaptersByBookId(bookId)
            comments = commentRepository.getCommentsByBookId(bookId)

            userManager.getUser().savedBooks.forEach {(key, value) ->
                if (value.contains(book!!.id)) {
                    bookmarkFolderName = key
                }
            }
        }
    }

    fun changeSortType() {
        chaptersSortType = !chaptersSortType
    }

    fun saveComment(text: String) {
        viewModelScope.launch {
            val user = userRepository.getUser()

            if (user?.id == 0u) {
                commentValidationEventChannel.send(CommentValidationEvent.Error)
                return@launch
            }

            val comment = Comment(
                bookId = book!!.id,
                userId = user!!.id,
                text = text,
                user = user,
            )

            val result = commentRepository.saveComment(comment)
            if (result.id == 0u) {
                commentValidationEventChannel.send(CommentValidationEvent.Error)
                return@launch
            }

            val mutable = comments!!.toMutableList()
            mutable.add(0, result)
            comments = mutable.toList()
            commentValidationEventChannel.send(CommentValidationEvent.Success)
        }
    }

    fun isAuth(): Boolean {
        val isAuth = runBlocking {
            return@runBlocking userRepository.getUser()
        }

        return !(isAuth == null || isAuth.id == 0u)
    }

    fun saveBookInBookmarks(folderName: String) {
        viewModelScope.launch {
            val user = userManager.getUser()
            val bookmarks = user.savedBooks as MutableMap
            if (!bookmarks[folderName]!!.contains(book!!.id)) {
                bookmarks[folderName]!!.add(book!!.id)
            }
            bookmarkFolderName = folderName

            userManager.setUser(
                user.copy(
                    savedBooks = bookmarks,
                ),
            )
        }
    }

    fun onRemoveBookFromBookmarks(folderName: String) {
        viewModelScope.launch {
            val user = userManager.getUser()
            val bookmarks = user.savedBooks as MutableMap
            if (bookmarks[folderName]!!.contains(book!!.id)) {
                bookmarks[folderName]!!.remove(book!!.id)
            }
            bookmarkFolderName = ""

            userManager.setUser(
                user.copy(
                    savedBooks = bookmarks.toMap(),
                ),
            )
        }
    }

    sealed class CommentValidationEvent {
        data object Success: CommentValidationEvent()
        data object Error: CommentValidationEvent()
    }
}
