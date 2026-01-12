package com.tondracek.myfarmer.ui.auth.loginscreen.components

import android.util.Patterns

data class LoginInput(
    val email: String,
    val password: String,
) {
    companion object {
        val Empty = LoginInput(
            email = "",
            password = "",
        )
    }
}

data class LoginValidation(
    val emailError: String? = null,
    val passwordError: String? = null,
) {

    companion object {
        val Valid = LoginValidation()
    }

    fun isValid() = this == Valid
}

fun validateInput(loginInput: LoginInput): LoginValidation {
    val emailResult = validateEmail(loginInput.email)
    val passwordResult = validatePassword(loginInput.password)

    return LoginValidation(
        emailError = emailResult,
        passwordError = passwordResult,
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
