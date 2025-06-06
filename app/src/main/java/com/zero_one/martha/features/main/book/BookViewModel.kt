package com.zero_one.martha.features.main.book

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.zero_one.martha.data.domain.model.Book
import com.zero_one.martha.data.domain.model.BookRate
import com.zero_one.martha.data.domain.model.Chapter
import com.zero_one.martha.data.domain.model.Comment
import com.zero_one.martha.data.domain.model.CommentRate
import com.zero_one.martha.data.domain.model.SavedBook
import com.zero_one.martha.data.domain.repository.BookRateRepository
import com.zero_one.martha.data.domain.repository.BookRepository
import com.zero_one.martha.data.domain.repository.ChapterRepository
import com.zero_one.martha.data.domain.repository.CommentRateRepository
import com.zero_one.martha.data.domain.repository.CommentRepository
import com.zero_one.martha.data.domain.repository.UserRepository
import com.zero_one.martha.data.source.datastore.user.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
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
    private val userManager: UserManager,
    private val commentRateRepository: CommentRateRepository,
    private val bookRateRepository: BookRateRepository,
): ViewModel() {
    var user = userManager.getUserFlow()
    var book: Book? by mutableStateOf(null)
    var chapters: List<Chapter>? by mutableStateOf(null)
    var chaptersSortType: Boolean by mutableStateOf(true)
    var comments: List<Comment>? by mutableStateOf(null)
    var bookmarkFolderName by mutableStateOf("")
    private var savedBook by mutableStateOf(SavedBook())

    private val _userBookRate = MutableStateFlow(BookRate())
    val userBookRate = _userBookRate.asStateFlow()

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
                value.forEach {
                    if (it.bookId == book!!.id) {
                        bookmarkFolderName = key
                        savedBook = it
                    }
                }
            }
            _userBookRate.update {
                book!!.rates.firstOrNull {
                    it.bookId == book!!.id &&
                        it.userId == user.first().id
                } ?: BookRate()
            }
        }
    }

    fun changeSortType() {
        chaptersSortType = !chaptersSortType
    }

    fun saveComment(text: String, parentId: UInt = 0u) {
        viewModelScope.launch {
            val user = userRepository.getUser()

            if (user?.id == 0u) {
                commentValidationEventChannel.send(CommentValidationEvent.Error)
                return@launch
            }

            val comment = Comment(
                parentId = parentId,
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

            if (result.parentId != 0u) {
                comments = commentRepository.getCommentsByBookId(book!!.id)
            } else {
                val mutable = comments!!.toMutableList()
                mutable.add(0, result)
                comments = mutable.toList()
            }
            commentValidationEventChannel.send(CommentValidationEvent.Success)
        }
    }

    fun deleteComment(commentId: UInt) {
        viewModelScope.launch {
            val deleteResult = commentRepository.deleteComment(commentId)
            if (deleteResult) {
                comments = commentRepository.getCommentsByBookId(book!!.id)
                commentValidationEventChannel.send(CommentValidationEvent.Success)
                return@launch
            }
            commentValidationEventChannel.send(CommentValidationEvent.Error)
        }
    }

    fun updateComment(commentId: UInt, text: String) {
        viewModelScope.launch {
            var comment = recursiveSearchComment(commentId, comments!!)

            comment = comment.copy(
                text = text,
            )

            val updateCommentResult = commentRepository.updateComment(comment)
            if (updateCommentResult.id == 0u) {
                commentValidationEventChannel.send(CommentValidationEvent.Error)
                return@launch
            }

            if (updateCommentResult.parentId != 0u) {
                comments = commentRepository.getCommentsByBookId(book!!.id)
                commentValidationEventChannel.send(CommentValidationEvent.Success)
                return@launch
            }
            val mutable = comments!!.toMutableList()
            mutable.forEachIndexed {index, value ->
                if (value.id == updateCommentResult.id) {
                    mutable[index] = value.copy(
                        text = updateCommentResult.text,
                    )
                }
            }
            comments = mutable.toList()
            commentValidationEventChannel.send(CommentValidationEvent.Success)
        }
    }

    fun onRateComment(commentId: UInt, rating: Boolean?) {
        viewModelScope.launch {
            val user = userManager.getUser()
            val comment = recursiveSearchComment(commentId, comments!!)

            val commentRate =
                comment.rates.firstOrNull {it.commentId == commentId && it.userId == user.id}

            if (commentRate != null) {
                if (rating == null) {
                    deleteCommentRate(commentRate)
                } else {
                    val newCommentRate = commentRate.copy(
                        rating = rating,
                    )
                    updateCommentRate(newCommentRate)
                }
            } else {
                createCommentRate(
                    CommentRate(
                        commentId = commentId,
                        userId = user.id,
                        rating = rating!!,
                    ),
                )
            }
        }
    }

    private fun createCommentRate(commentRate: CommentRate) {
        viewModelScope.launch {
            val createResult = commentRateRepository.createCommentRate(commentRate)
            if (createResult.commentId != 0u) {
                comments = commentRepository.getCommentsByBookId(book!!.id)
                commentValidationEventChannel.send(CommentValidationEvent.Success)
                return@launch
            }
            commentValidationEventChannel.send(CommentValidationEvent.Error)
        }
    }

    private fun updateCommentRate(commentRate: CommentRate) {
        viewModelScope.launch {
            val createResult = commentRateRepository.updateCommentRate(commentRate)
            if (createResult.commentId != 0u) {
                comments = commentRepository.getCommentsByBookId(book!!.id)
                commentValidationEventChannel.send(CommentValidationEvent.Success)
                return@launch
            }
            commentValidationEventChannel.send(CommentValidationEvent.Error)
        }
    }

    private fun deleteCommentRate(commentRate: CommentRate) {
        viewModelScope.launch {
            val createResult = commentRateRepository.deleteCommentRate(commentRate)
            if (createResult) {
                comments = commentRepository.getCommentsByBookId(book!!.id)
                commentValidationEventChannel.send(CommentValidationEvent.Success)
                return@launch
            }
            commentValidationEventChannel.send(CommentValidationEvent.Error)
        }
    }

    fun onRateBook(bookId: UInt, rating: Int) {
        viewModelScope.launch {
            val user = userManager.getUser()

            val bookRate = book!!.rates.firstOrNull {it.bookId == bookId && it.userId == user.id}

            if (bookRate != null) {
                if (rating == 0) {
                    onDeleteBookRate(bookRate)
                } else {
                    onUpdateBookRate(
                        bookRate.copy(
                            rating = rating,
                        ),
                    )
                }
            } else onCreateBookRate(
                BookRate(
                    bookId = bookId,
                    userId = user.id,
                    rating = rating,
                ),
            )
        }
    }

    private fun onCreateBookRate(bookRate: BookRate) {
        viewModelScope.launch {
            val createdBookRate = bookRateRepository.createBookRate(bookRate)
            if (createdBookRate.bookId != 0u) {
                val newRates = book!!.rates.toMutableList()
                newRates.add(createdBookRate)
                book = book!!.copy(
                    rates = newRates,
                )
                _userBookRate.update {createdBookRate}
            }
        }
    }

    private fun onUpdateBookRate(bookRate: BookRate) {
        viewModelScope.launch {
            val updatedBookRate = bookRateRepository.updateBookRate(bookRate)
            if (updatedBookRate.rating != 0) {
                val newRates = book!!.rates.toMutableList()
                newRates.removeIf {
                    it.bookId == updatedBookRate.bookId &&
                        it.userId == updatedBookRate.userId
                }
                newRates.add(updatedBookRate)
                book = book!!.copy(
                    rates = newRates,
                )
                _userBookRate.update {updatedBookRate}
            }
        }
    }

    private fun onDeleteBookRate(bookRate: BookRate) {
        viewModelScope.launch {
            val deletedBookRate = bookRateRepository.deleteBookRate(bookRate)
            if (deletedBookRate) {
                val newRates = book!!.rates.toMutableList()
                newRates.removeIf {it.bookId == bookRate.bookId && it.userId == bookRate.userId}
                book = book!!.copy(
                    rates = newRates,
                )
                _userBookRate.update {BookRate()}
            }
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
            val list = bookmarks[folderName]!!.toMutableList()

            if (list.none {it.bookId == book!!.id}) {
                list.add(
                    SavedBook(
                        bookId = book!!.id,
                    ),
                )
                bookmarks[folderName] = list
            }

            bookmarkFolderName = folderName

            userManager.setUser(
                user.copy(
                    savedBooks = bookmarks,
                ),
            )

            userRepository.updateUser(userManager.getUser())
        }
    }

    fun onReplaceBookmark(oldFolderName: String, newFolderName: String) {
        viewModelScope.launch {
            val user = userManager.getUser()
            val bookmarks = user.savedBooks as MutableMap
            val list = bookmarks[oldFolderName]!!.toMutableList()
            val savedBook = list.filter {it.bookId == book!!.id}.elementAt(0)
            list.removeIf {it.bookId == book!!.id}
            bookmarks[oldFolderName] = list
            bookmarks[newFolderName]!!.add(savedBook)
            userManager.setUser(
                user.copy(
                    savedBooks = bookmarks.toMap(),
                ),
            )
            bookmarkFolderName = newFolderName

            userRepository.updateUser(userManager.getUser())
        }
    }

    fun onRemoveBookFromBookmarks(folderName: String) {
        viewModelScope.launch {
            val user = userManager.getUser()
            val bookmarks = user.savedBooks as MutableMap
            val list = bookmarks[folderName]!!.toMutableList()
            list.removeIf {
                it.bookId == book!!.id
            }
            bookmarks[folderName] = list
            bookmarkFolderName = ""

            userManager.setUser(
                user.copy(
                    savedBooks = bookmarks.toMap(),
                ),
            )

            userRepository.updateUser(userManager.getUser())
        }
    }

    private fun recursiveSearchComment(id: UInt, comments: List<Comment>): Comment {
        comments.forEach {comment ->
            if (comment.replies.isNotEmpty()) {
                return comment.replies.firstOrNull {it.id == id} ?: recursiveSearchComment(
                    id,
                    comment.replies,
                )
            }
        }

        return Comment()
    }

    sealed class CommentValidationEvent {
        data object Success: CommentValidationEvent()
        data object Error: CommentValidationEvent()
    }
}
