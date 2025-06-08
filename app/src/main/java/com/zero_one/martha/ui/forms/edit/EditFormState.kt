package com.zero_one.martha.ui.forms.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.zero_one.martha.ui.fields.email.EmailFieldState
import com.zero_one.martha.ui.fields.email.rememberEmailFieldState
import com.zero_one.martha.ui.fields.username.UsernameFieldState
import com.zero_one.martha.ui.fields.username.rememberUsernameFieldState

@Stable
class EditFormState(
    val email: EmailFieldState,
    val username: UsernameFieldState
) {
    val isValid: Boolean by derivedStateOf {
        email.isValid && username.isValid
    }
    
    fun validate() {
        email.validate()
        username.validate()
    }
}

@Composable
fun rememberEditFormState(): EditFormState {
    val email = rememberEmailFieldState()
    val username = rememberUsernameFieldState()

    return remember {
        EditFormState(email, username)
    }
}
