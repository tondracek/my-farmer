package com.tondracek.myfarmer.core.domain.domainerror

sealed interface DomainError

sealed interface AuthError : DomainError {
    data object NotLoggedIn : AuthError
    data object UserNotFound : AuthError
    data object InvalidCredentials : AuthError
    data object EmailAlreadyInUse : AuthError
    data object WeakPassword : AuthError

    data object GoogleSignInFailedError : DomainError
    data object GoogleSignInCancelledError : DomainError

    data object LogoutFailed : AuthError

    data object Forbidden : AuthError

    data object Unknown : AuthError
}

sealed interface PhotoError : DomainError {
    data object UploadFailed : PhotoError
    data object DeletionFailed : PhotoError
}

sealed interface ShopError : DomainError {

    data object CreationFailed : ShopError
    data object UpdateFailed : ShopError
    data object DeletionFailed : ShopError
    data object FetchingFailed : ShopError

    data object NotOwner : ShopError
    data object NotFound : ShopError

    data object Unknown : ShopError
}

sealed interface UserError : DomainError {
    data object CreationFailed : UserError
    data object UpdateFailed : UserError
    data object DeletionFailed : UserError

    data object FetchingFailed : UserError
    data object NotFound : UserError

    data object Unknown : UserError
}

sealed interface ReviewError : DomainError {

    data object CreationFailed : ReviewError
    data object UpdateFailed : ReviewError
    data object DeletionFailed : ReviewError

    data object FetchingFailed : ReviewError
    data object NotFound : ReviewError
    data object AlreadyExists : ReviewError
    data object NotAuthor : ReviewError
    data object CreatingToOwnShopNotAllowed : ReviewError

    data object Unknown : ReviewError
}

sealed interface CategoryPopularityError : DomainError {
    data object FetchingFailed : CategoryPopularityError
    data object Unknown : CategoryPopularityError
}

sealed interface InputDataError : DomainError {
    data object MissingLocationInput : InputDataError
}

sealed interface ValidationError : DomainError {
    /** Email */
    data object InvalidEmailFormat : ValidationError

    /** Password */
    data object PasswordMustBeAtLeast6Chars : ValidationError
    data object PasswordMustContainUppercaseLetter : ValidationError
    data object PasswordMustContainLowercaseLetter : ValidationError

    /** Confirm Password */
    data object PasswordsDoNotMatch : ValidationError
}
