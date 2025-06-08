package com.zero_one.martha.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.zero_one.martha.R

@Composable
fun parseBookStatus(value: String): String {
    return when (value) {
        "Ended" -> stringResource(R.string.status_ended)
        "Ongoing" -> stringResource(R.string.status_ongoing)
        "Stopped" -> stringResource(R.string.status_stopped)
        else -> value
    }
}
