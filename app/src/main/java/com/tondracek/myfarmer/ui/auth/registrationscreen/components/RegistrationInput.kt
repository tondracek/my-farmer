package com.tondracek.myfarmer.ui.auth.registrationscreen.components

import android.util.Patterns
import com.tondracek.myfarmer.core.domain.domainerror.ValidationError


data class RegistrationInput(
    val email: String,
    val password: String,
    val confirmPassword: String,
) {
    companion object {
        val Empty = RegistrationInput(
            email = "",
            password = "",
            confirmPassword = "",
        )
    }
}

data class RegistrationValidation(
    val emailError: ValidationError? = null,
    val passwordError: ValidationError? = null,
    val confirmPasswordError: ValidationError? = null,
) {

    fun isValid() = this == Valid

    companion object {
        val Valid = RegistrationValidation()
    }
}

fun validateInput(registrationInput: RegistrationInput): RegistrationValidation {
    val emailResult = validateEmail(registrationInput.email)
    val confirmPasswordResult =
        validateConfirmPassword(registrationInput.password, registrationInput.confirmPassword)
    val passwordResult = validatePassword(registrationInput.password)

    return RegistrationValidation(
        emailError = emailResult,
        passwordError = passwordResult,
        confirmPasswordError = confirmPasswordResult,
    )
}


private fun validateEmail(email: String): ValidationError? =
    when (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        true -> null
        false -> ValidationError.InvalidEmailFormat
    }

private fun validatePassword(password: String): ValidationError? {
    // Minimum length check
    if (password.length < 6) return ValidationError.PasswordMustBeAtLeast6Chars
    // Uppercase letter check
    if (!password.any { it.isUpperCase() }) return ValidationError.PasswordMustContainUppercaseLetter
    // Lowercase letter check
    if (!password.any { it.isLowerCase() }) return ValidationError.PasswordMustContainLowercaseLetter

    return null
}

private fun validateConfirmPassword(password: String, confirmPassword: String): ValidationError? =
    when (password == confirmPassword) {
        true -> null
        false -> ValidationError.PasswordsDoNotMatch
    }
