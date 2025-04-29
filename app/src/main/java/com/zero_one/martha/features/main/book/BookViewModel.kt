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
    private val userRepository: UserRepository
): ViewModel() {
    var book: Book? by mutableStateOf(null)
    var chapters: List<Chapter>? by mutableStateOf(null)
    var chaptersSortType: Boolean by mutableStateOf(true)
    var comments: List<Comment>? by mutableStateOf(null)

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
        }
    }

    fun changeSortType() {
        chaptersSortType = !chaptersSortType
    }

    fun saveComment(text: String) {
        viewModelScope.launch {
            val userId = userRepository.getUser()?.id

            if (userId == 0u) {
                commentValidationEventChannel.send(CommentValidationEvent.Error)
                return@launch
            }

            val comment = Comment(
                bookId = book!!.id,
                userId = userId!!,
                text = text,
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

    sealed class CommentValidationEvent {
        data object Success: CommentValidationEvent()
        data object Error: CommentValidationEvent()
    }
}
