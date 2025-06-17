package com.zero_one.martha.data.validators

fun validatePassword(password: String): PasswordValidationResult {
    val specialCharacters = "&*()=+{[]}\\|/?'\";:,.^%#№@!~`$"
    val hasDigitsAndLetters = password.any {it.isDigit()} && password.any {it.isLetter()}
    return when {
        password.isEmpty() -> PasswordValidationResult.EMPTY
        password.length < 8 || password.length > 64 -> PasswordValidationResult.INVALID_LENGTH
        !hasDigitsAndLetters || !password.any {it in specialCharacters} -> PasswordValidationResult.INVALID
        else -> PasswordValidationResult.VALID
    }
}

fun isPasswordValid(password: String): Boolean {
    return validatePassword(password) == PasswordValidationResult.VALID
}

enum class PasswordValidationResult {
    VALID,
    EMPTY,
    INVALID_LENGTH,
    INVALID
}
