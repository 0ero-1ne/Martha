package com.zero_one.martha.utils

import android.content.Context
import com.zero_one.martha.R

fun parseUsernameFieldError(string: String?, context: Context): String? {
    return when (string) {
        "invalid" -> context.resources.getString(R.string.invalid_username)
        "invalid_length" -> context.resources.getString(R.string.invalid_length_username)
        "empty" -> context.resources.getString(R.string.empty_username)
        else -> string
    }
}

fun parseEmailFieldError(string: String?, context: Context): String? {
    return when (string) {
        "invalid" -> context.resources.getString(R.string.invalid_email)
        "empty" -> context.resources.getString(R.string.empty_email)
        else -> string
    }
}

fun parsePasswordFieldError(string: String?, context: Context): String? {
    return when (string) {
        "invalid" -> context.resources.getString(R.string.invalid_password)
        "invalid_length" -> context.resources.getString(R.string.invalid_length_password)
        "empty" -> context.resources.getString(R.string.empty_password)
        else -> string
    }
}

fun parseCommentFieldError(string: String?, context: Context): String? {
    return when (string) {
        "empty" -> context.resources.getString(R.string.empty_comment)
        else -> string
    }
}
