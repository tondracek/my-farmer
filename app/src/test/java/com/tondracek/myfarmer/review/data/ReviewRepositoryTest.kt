package com.tondracek.myfarmer.review.data

import com.tondracek.myfarmer.core.repository.fake.FakeRepositoryCoreFactory
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.Review
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class ReviewRepositoryTest {

    val shopId: UUID = UUID.randomUUID()
    val userId: UUID = UUID.randomUUID()

    val reviewRepository: ReviewRepository = ReviewRepository(
        core = FakeRepositoryCoreFactory<ReviewEntity> { UUID.fromString(this.id) }
            .create(ReviewMapper(), ReviewEntity::class.java)
    )

    private suspend fun insert(review: Review) {
        reviewRepository.create(review)
    }

    @Test
    fun `getReviewsPreview returns limited number of reviews`() = runTest {
        repeat(20) {
            insert(
                Review(
                    id = UUID.randomUUID(),
                    userId = userId,
                    shopId = shopId,
                    rating = Rating(5),
                    comment = "Review $it",
                )
            )
        }

        val previews = reviewRepository
            .getReviewsPreview(shopId)
            .first()

        assertTrue(previews.size <= 10)
    }

    @Test
    fun `getReviews filters by shop`() = runTest {
        val shopA = UUID.randomUUID()
        val shopB = UUID.randomUUID()

        insert(Review(UUID.randomUUID(), shopA, userId, Rating(5), "A1"))
        insert(Review(UUID.randomUUID(), shopA, userId, Rating(4), "A2"))
        insert(Review(UUID.randomUUID(), shopB, userId, Rating(3), "B1"))

        val results = reviewRepository
            .getReviews(shopA)
            .first()

        assertEquals(2, results.size)
        assertTrue(results.all { it.shopId == shopA })
    }

    @Test
    fun `getReviews applies offset and limit`() = runTest {
        val ids = List(5) { UUID.randomUUID() }

        ids.forEachIndexed { index, id ->
            insert(Review(id, shopId, userId, Rating(5), "R$index"))
        }

        val results = reviewRepository
            .getReviews(shopId, offset = 1, limit = 2)
            .first()

        assertEquals(2, results.size)
        assertTrue(results.map { it.comment }.containsAll(setOf("R1", "R2")))
    }
}
