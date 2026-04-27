package com.tondracek.myfarmer.common.validation

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.core.domain.domainerror.ValidationError
import org.junit.Test

class ValidationTest {

    @Test
    fun `validatePassword returns length error for short password`() {
        val err = validatePassword("Abc1")
        assertThat(err).isEqualTo(ValidationError.PasswordMustBeAtLeast6Chars)
    }

    @Test
    fun `validatePassword returns uppercase error when no uppercase`() {
        val err = validatePassword("abcdef")
        assertThat(err).isEqualTo(ValidationError.PasswordMustContainUppercaseLetter)
    }

    @Test
    fun `validatePassword returns lowercase error when no lowercase`() {
        val err = validatePassword("ABCDEF")
        assertThat(err).isEqualTo(ValidationError.PasswordMustContainLowercaseLetter)
    }

    @Test
    fun `validatePassword returns null for valid password`() {
        val err = validatePassword("Abcdef")
        assertThat(err).isNull()
    }

    @Test
    fun `validateConfirmPassword returns null when equal and error when not`() {
        assertThat(validateConfirmPassword("x", "x")).isNull()
        assertThat(validateConfirmPassword("x", "y")).isEqualTo(ValidationError.PasswordsDoNotMatch)
    }

    @Test
    fun `validateEmail returns null when pattern matches and error when not`() {
        assertThat(validateEmail("john.123@example.com")).isNull()
        assertThat(validateEmail("invalid-email")).isEqualTo(ValidationError.InvalidEmailFormat)
    }
}



