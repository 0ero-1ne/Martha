package com.zero_one.martha.data.validators

fun validateUsername(username: String): UsernameValidationResult {
    val badCharacters = "&*()=+{[]}\\|/?'\";:,.^%#№@!~`"
    val hasDigitsAndLetters = username.any {it.isDigit()} && username.any {it.isLetter()}
    return when {
        username.isEmpty() -> UsernameValidationResult.EMPTY
        username.length < 6 -> UsernameValidationResult.INVALID_LENGTH
        !hasDigitsAndLetters || username.any {it in badCharacters} -> UsernameValidationResult.INVALID
        else -> UsernameValidationResult.VALID
    }
}

fun isUsernameValid(username: String): Boolean {
    return validateUsername(username) == UsernameValidationResult.VALID
}

enum class UsernameValidationResult {
    VALID,
    EMPTY,
    INVALID,
    INVALID_LENGTH
}
