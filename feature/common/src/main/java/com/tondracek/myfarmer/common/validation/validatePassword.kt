package com.tondracek.myfarmer.common.validation

import com.tondracek.myfarmer.core.domain.domainerror.ValidationError

fun validatePassword(password: String): ValidationError? {
    // Minimum length check
    if (password.length < 6) return ValidationError.PasswordMustBeAtLeast6Chars
    // Uppercase letter check
    if (!password.any { it.isUpperCase() }) return ValidationError.PasswordMustContainUppercaseLetter
    // Lowercase letter check
    if (!password.any { it.isLowerCase() }) return ValidationError.PasswordMustContainLowercaseLetter

    return null
}

