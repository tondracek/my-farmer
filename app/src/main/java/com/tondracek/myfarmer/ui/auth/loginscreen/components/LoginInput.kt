package com.tondracek.myfarmer.ui.auth.loginscreen.components

import android.util.Patterns
import com.tondracek.myfarmer.core.domain.domainerror.ValidationError

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
    val emailError: ValidationError? = null,
    val passwordError: ValidationError? = null,
) {

    companion object {
        val Valid = LoginValidation()
    }

    fun isValid() = this == Valid
}

fun validateInput(loginInput: LoginInput): LoginValidation {
    val emailResult = validateEmail(loginInput.email)
    val passwordResult = null

    return LoginValidation(
        emailError = emailResult,
        passwordError = passwordResult,
    )
}


private fun validateEmail(email: String): ValidationError? =
    when (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        true -> null
        false -> ValidationError.InvalidEmailFormat
    }