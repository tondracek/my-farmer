package com.tondracek.myfarmer.common.validation

import com.tondracek.myfarmer.core.domain.domainerror.ValidationError

private val EMAIL_REGEX: Regex = (
        "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
                "([A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z]{2,}$"
        ).toRegex()

fun validateEmail(email: String): ValidationError? {
    val normalized = email.trim()

    if (normalized.length > 254) {
        return ValidationError.InvalidEmailFormat
    }

    return when (EMAIL_REGEX.matches(normalized)) {
        true -> null
        false -> ValidationError.InvalidEmailFormat
    }
}

