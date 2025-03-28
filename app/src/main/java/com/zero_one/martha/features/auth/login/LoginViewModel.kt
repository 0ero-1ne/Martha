package com.zero_one.martha.features.auth.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.zero_one.martha.data.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val repository: AuthRepository
) : ViewModel() {

    fun login(email: String, password: String) {

    }
}
