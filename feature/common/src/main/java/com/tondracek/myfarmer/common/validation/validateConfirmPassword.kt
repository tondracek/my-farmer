package com.tondracek.myfarmer.common.validation

import com.tondracek.myfarmer.core.domain.domainerror.ValidationError

fun validateConfirmPassword(password: String, confirmPassword: String): ValidationError? =
    when (password == confirmPassword) {
        true -> null
        false -> ValidationError.PasswordsDoNotMatch
    }
