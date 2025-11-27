package com.tondracek.myfarmer.ui.myshopsscreen

import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory

sealed interface MyShopsState {

    data class Success(val shops: List<MyShopsListItem>) : MyShopsState

    data object Loading : MyShopsState

    data class Error(val failure: UCResult.Failure) : MyShopsState
}

data class MyShopsListItem(
    val id: ShopId,
    val name: String?,
    val description: String?,
    val categories: List<ShopCategory>,
    val image: ImageResource?,
    val averageRating: Rating,
)

fun Shop.toMyShopsListItem(
    averageRating: Rating,
) = MyShopsListItem(
    id = id,
    image = images.firstOrNull(),
    name = name,
    categories = categories,
    averageRating = averageRating,
    description = description,
)