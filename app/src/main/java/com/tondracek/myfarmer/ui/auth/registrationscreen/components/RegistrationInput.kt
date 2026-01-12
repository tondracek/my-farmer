package com.tondracek.myfarmer.ui.auth.registrationscreen.components

import android.util.Patterns


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
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
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


private fun validateEmail(email: String): String? =
    when (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        true -> null
        false -> "Invalid email address."
    }

private fun validatePassword(password: String): String? {
    // Minimum length check
    if (password.length < 6) return "Password must be at least 6 characters long."
    // Uppercase letter check
    if (!password.any { it.isUpperCase() }) return "Password must contain at least one uppercase letter."
    // Lowercase letter check
    if (!password.any { it.isLowerCase() }) return "Password must contain at least one lowercase letter."

    return null
}

private fun validateConfirmPassword(password: String, confirmPassword: String): String? =
    when (password == confirmPassword) {
        true -> null
        false -> "Passwords do not match."
    }
