package com.zero_one.martha.features.main.profile.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zero_one.martha.data.domain.model.User
import com.zero_one.martha.data.domain.repository.AuthRepository
import com.zero_one.martha.data.domain.repository.StorageRepository
import com.zero_one.martha.data.domain.repository.UserRepository
import com.zero_one.martha.data.source.datastore.user.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userManager: UserManager,
    private val userRepository: UserRepository,
    private val storageRepository: StorageRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    var isLoading by mutableStateOf(false)
    var editErrorMessage = ""

    private val editValidationEventChannel = Channel<EditValidationEvent>()
    val editValidationEvents = editValidationEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            _user.update {userManager.getUser()}
        }
    }

    fun uploadImage(file: File) {
        viewModelScope.launch {
            isLoading = true
            val uploadResult = storageRepository.uploadImage(file)
            if (uploadResult.error == null) {
                val user = _user.value.copy(
                    image = uploadResult.data!!,
                )
                val updateUser = userRepository.updateUser(user)
                if (updateUser.status) {
                    storageRepository.deleteImage(_user.value.image)
                    _user.update {updateUser.user!!}
                    editValidationEventChannel.send(EditValidationEvent.ImageSuccess)
                } else {
                    editErrorMessage = updateUser.message
                    editValidationEventChannel.send(EditValidationEvent.ImageError)
                }
                isLoading = false

                return@launch
            }
            editErrorMessage = uploadResult.error
            editValidationEventChannel.send(EditValidationEvent.ImageError)
            isLoading = false
        }
    }

    fun onSave(email: String, username: String) {
        viewModelScope.launch {
            editErrorMessage = ""
            isLoading = true
            val newUser = _user.value.copy(
                email = email,
                username = username,
            )

            val updateResult = userRepository.updateUser(newUser)
            if (updateResult.status) {
                _user.update {updateResult.user!!}
                editValidationEventChannel.send(EditValidationEvent.EditSuccess)
            } else {
                if (updateResult.message.contains("duplicate key")
                    && updateResult.message.contains("users_email_key")
                ) {
                    editErrorMessage = "Email is already in use"
                }
                if (updateResult.message.contains("duplicate key")
                    && updateResult.message.contains("users_username_key")
                ) {
                    editErrorMessage = "Username is already in use"
                }
                editValidationEventChannel.send(EditValidationEvent.EditError)
            }
            isLoading = false
        }
    }

    fun onChangePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            isLoading = true
            val changePasswordResult = authRepository.changePassword(
                oldPassword = oldPassword,
                newPassword = newPassword,
            )

            if (changePasswordResult == null) {
                editValidationEventChannel.send(EditValidationEvent.PasswordSuccess)
                isLoading = false
                return@launch
            }
            editErrorMessage = changePasswordResult
            editValidationEventChannel.send(EditValidationEvent.PasswordError)
            isLoading = false
        }
    }

    sealed class EditValidationEvent {
        data object EditSuccess: EditValidationEvent()
        data object EditError: EditValidationEvent()
        data object PasswordSuccess: EditValidationEvent()
        data object PasswordError: EditValidationEvent()
        data object ImageSuccess: EditValidationEvent()
        data object ImageError: EditValidationEvent()
    }
}
