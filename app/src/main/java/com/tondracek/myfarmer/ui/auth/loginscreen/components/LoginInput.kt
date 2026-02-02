package com.tondracek.myfarmer.ui.auth.loginscreen.components

import com.tondracek.myfarmer.common.validation.validateEmail
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
    val emailError = validateEmail(loginInput.email)
    val passwordError = null

    return LoginValidation(
        emailError = emailError,
        passwordError = passwordError,
    )
}
