package com.zero_one.martha.data.validators

import android.util.Patterns

fun validateEmail(email: String): EmailValidationResult {
    return when {
        email.isEmpty() -> EmailValidationResult.EMPTY
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> EmailValidationResult.INVALID
        else -> EmailValidationResult.VALID
    }
}

fun isEmailValid(email: String): Boolean {
    return validateEmail(email) == EmailValidationResult.VALID
}

enum class EmailValidationResult {
    VALID,
    EMPTY,
    INVALID
}
