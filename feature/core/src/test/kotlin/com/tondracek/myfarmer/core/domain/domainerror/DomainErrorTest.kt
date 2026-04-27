package com.tondracek.myfarmer.core.domain.domainerror

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DomainErrorTest {

    @Test
    fun `all domain errors are instances and have stable equality`() {
        val all: List<DomainError> = listOf(
            // AuthError
            AuthError.NotLoggedIn,
            AuthError.UserNotFound,
            AuthError.InvalidCredentials,
            AuthError.EmailAlreadyInUse,
            AuthError.WeakPassword,
            AuthError.GoogleSignInFailedError,
            AuthError.GoogleSignInCancelledError,
            AuthError.LogoutFailed,
            AuthError.Forbidden,
            AuthError.Unknown,

            // PhotoError
            PhotoError.UploadFailed,
            PhotoError.DeletionFailed,

            // LocationError
            LocationError.FetchingFailed,
            LocationError.MissingLocationPermission,

            // ShopError
            ShopError.CreationFailed,
            ShopError.UpdateFailed,
            ShopError.DeletionFailed,
            ShopError.FetchingFailed,
            ShopError.NotOwner,
            ShopError.NotFound,
            ShopError.Unknown,

            // UserError
            UserError.CreationFailed,
            UserError.UpdateFailed,
            UserError.DeletionFailed,
            UserError.FetchingFailed,
            UserError.NotFound,
            UserError.Unknown,

            // ReviewError
            ReviewError.CreationFailed,
            ReviewError.UpdateFailed,
            ReviewError.DeletionFailed,
            ReviewError.FetchingFailed,
            ReviewError.NotFound,
            ReviewError.AlreadyExists,
            ReviewError.NotAuthor,
            ReviewError.CreatingToOwnShopNotAllowed,
            ReviewError.Unknown,

            // CategoryPopularityError
            CategoryPopularityError.FetchingFailed,
            CategoryPopularityError.Unknown,

            // InputDataError
            InputDataError.MissingLocationInput,

            // ValidationError
            ValidationError.InvalidEmailFormat,
            ValidationError.PasswordMustBeAtLeast6Chars,
            ValidationError.PasswordMustContainUppercaseLetter,
            ValidationError.PasswordMustContainLowercaseLetter,
            ValidationError.PasswordsDoNotMatch,
            ValidationError.PrivacyPolicyNotAccepted
        )

        // basic sanity
        assertThat(all).isNotEmpty()

        // every item must be a DomainError
        all.forEach { e ->
            assertThat(e).isInstanceOf(DomainError::class.java)
            // equals to itself
            assertThat(e).isEqualTo(e)
        }

        // distinctness: two different objects are not equal (pick two known different ones)
        assertThat(AuthError.NotLoggedIn).isNotEqualTo(AuthError.UserNotFound)
        assertThat(ShopError.NotFound).isNotEqualTo(UserError.NotFound)
    }
}

