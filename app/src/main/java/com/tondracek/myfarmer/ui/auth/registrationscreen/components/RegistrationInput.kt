package com.tondracek.myfarmer.ui.auth.registrationscreen.components

import com.tondracek.myfarmer.common.validation.validateConfirmPassword
import com.tondracek.myfarmer.common.validation.validateEmail
import com.tondracek.myfarmer.common.validation.validatePassword
import com.tondracek.myfarmer.core.domain.domainerror.ValidationError


data class RegistrationInput(
    val email: String,
    val password: String,
    val confirmPassword: String,
    val privacyPolicyAccepted: Boolean,
) {
    companion object {
        val Empty = RegistrationInput(
            email = "",
            password = "",
            confirmPassword = "",
            privacyPolicyAccepted = false,
        )
    }
}

data class RegistrationValidation(
    val emailError: ValidationError? = null,
    val passwordError: ValidationError? = null,
    val confirmPasswordError: ValidationError? = null,
    val privacyPolicyError: ValidationError? = null,
) {

    fun isValid() = this == Valid

    companion object {
        val Valid = RegistrationValidation()
    }
}

fun validateInput(registrationInput: RegistrationInput): RegistrationValidation {
    val emailError = validateEmail(registrationInput.email)
    val confirmPasswordError =
        validateConfirmPassword(registrationInput.password, registrationInput.confirmPassword)
    val passwordError = validatePassword(registrationInput.password)

    val privacyPolicyError = when {
        registrationInput.privacyPolicyAccepted -> null
        else -> ValidationError.PrivacyPolicyNotAccepted
    }

    return RegistrationValidation(
        emailError = emailError,
        passwordError = passwordError,
        confirmPasswordError = confirmPasswordError,
        privacyPolicyError = privacyPolicyError,
    )
}