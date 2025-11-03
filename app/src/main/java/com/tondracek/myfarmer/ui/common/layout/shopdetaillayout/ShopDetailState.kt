package com.tondracek.myfarmer.ui.common.layout.shopdetaillayout

import com.tondracek.myfarmer.common.model.ImageResource
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.shoplocation.domain.model.ShopLocation
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser

sealed interface ShopDetailState {

    data class Success(
        val id: ShopId,
        val name: String?,
        val description: String?,
        val owner: SystemUser,
        val categories: List<ShopCategory>,
        val images: List<ImageResource>,
        val menu: ProductMenu,
        val location: ShopLocation,
        val openingHours: OpeningHours,
        val reviewsPreview: List<Review>,
        val averageRating: Rating,
    ) : ShopDetailState

    data class Error(val result: UCResult.Failure) : ShopDetailState

    data object Loading : ShopDetailState
}

fun Shop.toShopDetailState(
    owner: SystemUser,
    reviewsPreview: List<Review>,
    averageRating: Rating,
): ShopDetailState.Success =
    ShopDetailState.Success(
        id = this.id,
        name = this.name,
        description = this.description,
        owner = owner,
        categories = this.categories,
        images = this.images,
        menu = this.menu,
        location = this.location,
        openingHours = this.openingHours,
        reviewsPreview = reviewsPreview,
        averageRating = averageRating,
    )
