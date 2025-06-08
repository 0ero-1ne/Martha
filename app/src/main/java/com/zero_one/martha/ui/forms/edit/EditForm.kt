package com.zero_one.martha.ui.forms.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.zero_one.martha.ui.fields.email.EmailField
import com.zero_one.martha.ui.fields.username.UsernameField

@Composable
fun EditForm(
    state: EditFormState = rememberEditFormState()
) {
    Column {
        EmailField(
            modifier = Modifier
                .fillMaxWidth(),
            imeAction = ImeAction.Done,
            state = state.email,
        )
        UsernameField(
            modifier = Modifier
                .fillMaxWidth(),
            imeAction = ImeAction.Done,
            state = state.username,
        )
    }
}
