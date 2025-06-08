package com.zero_one.martha.features.main.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zero_one.martha.data.domain.model.Comment
import com.zero_one.martha.data.domain.model.User
import com.zero_one.martha.data.domain.repository.AuthRepository
import com.zero_one.martha.data.domain.repository.CommentRepository
import com.zero_one.martha.data.domain.repository.UserRepository
import com.zero_one.martha.data.source.datastore.user.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val userManager: UserManager,
    private val commentRepository: CommentRepository
): ViewModel() {
    var user: User? by mutableStateOf(null)
    private val _userComments = MutableStateFlow<List<Comment>?>(null)
    val userComments = _userComments.asStateFlow()

    init {
        viewModelScope.launch {
            _userComments.update {commentRepository.getUserComments(userManager.getUser().id)}
        }
    }

    fun isLogged() {
        runBlocking {
            Log.d("Is logged", "Is logged")
            user = userRepository.getUser()
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
