package com.tondracek.myfarmer.common.validation

import android.util.Patterns
import com.tondracek.myfarmer.core.domain.domainerror.ValidationError


fun validateEmail(email: String): ValidationError? =
    when (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        true -> null
        false -> ValidationError.InvalidEmailFormat
    }

