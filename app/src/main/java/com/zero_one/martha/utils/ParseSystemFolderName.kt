package com.zero_one.martha.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.zero_one.martha.R

@Composable
fun parseSystemFolderName(value: String): String {
    return when (value) {
        "Ended" -> stringResource(R.string.ended_folder)
        "Reading" -> stringResource(R.string.reading_folder)
        "Favorites" -> stringResource(R.string.favorites_folder)
        "Stopped" -> stringResource(R.string.stopped_folder)
        "Planed" -> stringResource(R.string.planned_folder)
        else -> value
    }
}
