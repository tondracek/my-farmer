package com.tondracek.myfarmer.ui.core.util

import android.content.Context
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.core.domain.domainerror.AuthError
import com.tondracek.myfarmer.core.domain.domainerror.CategoryPopularityError
import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.domainerror.InputDataError
import com.tondracek.myfarmer.core.domain.domainerror.PhotoError
import com.tondracek.myfarmer.core.domain.domainerror.ReviewError
import com.tondracek.myfarmer.core.domain.domainerror.ShopError
import com.tondracek.myfarmer.core.domain.domainerror.UserError
import com.tondracek.myfarmer.core.domain.domainerror.ValidationError

fun DomainError.toUserFriendlyMessage(context: Context) = when (this) {
    is AuthError.NotLoggedIn -> context.getString(R.string.you_are_not_logged_in)
    is AuthError.UserNotFound -> context.getString(R.string.user_not_found)
    is AuthError.InvalidCredentials -> context.getString(R.string.invalid_credentials)
    is AuthError.EmailAlreadyInUse -> context.getString(R.string.email_is_already_in_use)
    is AuthError.WeakPassword -> context.getString(R.string.the_password_is_too_weak)
    is AuthError.LogoutFailed -> context.getString(R.string.failed_to_log_out)
    AuthError.Forbidden -> context.getString(R.string.you_do_not_have_permission_to_perform_this_action)
    is AuthError.Unknown -> context.getString(R.string.an_unknown_authentication_error_occurred)

    is PhotoError.UploadFailed -> context.getString(R.string.failed_to_upload_photo)
    is PhotoError.DeletionFailed -> context.getString(R.string.failed_to_delete_photo)

    is ShopError.CreationFailed -> context.getString(R.string.failed_to_create_shop)
    is ShopError.UpdateFailed -> context.getString(R.string.failed_to_update_shop)
    is ShopError.DeletionFailed -> context.getString(R.string.failed_to_delete_shop)
    is ShopError.FetchingFailed -> context.getString(R.string.failed_to_fetch_shops)
    is ShopError.NotOwner -> context.getString(R.string.you_are_not_the_owner_of_this_shop)
    is ShopError.NotFound -> context.getString(R.string.shop_not_found)
    is ShopError.Unknown -> context.getString(R.string.an_unknown_shop_error_occurred)

    is UserError.CreationFailed -> context.getString(R.string.failed_to_create_user)
    is UserError.UpdateFailed -> context.getString(R.string.failed_to_update_user)
    is UserError.DeletionFailed -> context.getString(R.string.failed_to_delete_user)
    is UserError.FetchingFailed -> context.getString(R.string.failed_to_fetch_user)
    is UserError.NotFound -> context.getString(R.string.user_not_found)
    is UserError.Unknown -> context.getString(R.string.an_unknown_user_error_occurred)

    is ReviewError.CreationFailed -> context.getString(R.string.failed_to_create_review)
    is ReviewError.UpdateFailed -> context.getString(R.string.failed_to_update_review)
    is ReviewError.DeletionFailed -> context.getString(R.string.failed_to_delete_review)
    is ReviewError.FetchingFailed -> context.getString(R.string.failed_to_fetch_reviews)
    is ReviewError.NotFound -> context.getString(R.string.review_not_found)
    is ReviewError.Unknown -> context.getString(R.string.an_unknown_review_error_occurred)

    is CategoryPopularityError.FetchingFailed -> context.getString(R.string.failed_to_fetch_category_popularity)
    is CategoryPopularityError.Unknown -> context.getString(R.string.an_unknown_category_popularity_error_occurred)

    InputDataError.MissingRequiredData -> context.getString(R.string.required_data_is_missing)

    ValidationError.InvalidEmailFormat -> context.getString(R.string.invalid_email_format)
    ValidationError.PasswordMustBeAtLeast6Chars -> context.getString(R.string.password_must_be_at_least_6_characters_long)
    ValidationError.PasswordMustContainLowercaseLetter -> context.getString(R.string.password_must_contain_at_least_one_lowercase_letter)
    ValidationError.PasswordMustContainUppercaseLetter -> context.getString(R.string.password_must_contain_at_least_one_uppercase_letter)
    ValidationError.PasswordsDoNotMatch -> context.getString(R.string.passwords_do_not_match)
}