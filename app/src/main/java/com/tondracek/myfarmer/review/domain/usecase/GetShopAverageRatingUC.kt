package com.tondracek.myfarmer.review.domain.usecase

import android.util.Log
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.review.data.ReviewRepository
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.usecase.result.UCResultErrorLoadingReviews
import com.tondracek.myfarmer.shop.domain.model.ShopId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.math.roundToInt

class GetShopAverageRatingUC @Inject constructor(
    private val repository: ReviewRepository
) {
    operator fun invoke(shopId: ShopId): Flow<UCResult<Rating>> =
        repository.getReviews(shopId = shopId)
            .onEach { println(it) }
            .map<List<Review>, UCResult<Rating>> { reviews ->
                val ratings = reviews.map { it.rating.stars }
                val averageStars = ratings.takeIf { it.isNotEmpty() }?.average() ?: 0.0
                val rating = Rating(stars = averageStars.roundToInt())
                UCResult.Success(rating)
            }
            .catch { e ->
                emit(UCResultErrorLoadingReviews(e))
                Log.e(this.javaClass.name, e.message, e)
            }
}


