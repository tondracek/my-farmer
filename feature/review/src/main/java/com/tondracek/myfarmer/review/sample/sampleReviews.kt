package com.tondracek.myfarmer.review.sample

import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.shop.sample.shop0
import com.tondracek.myfarmer.shop.sample.shop2
import com.tondracek.myfarmer.shop.sample.shop3
import com.tondracek.myfarmer.user.sample.user0
import com.tondracek.myfarmer.user.sample.user1
import com.tondracek.myfarmer.user.sample.user2

val shop0reviews = listOf(
    Review(
        ReviewId.fromString("ef142e15-0e63-4e58-b8dc-2d05e00a47c7"),
        shop0.id,
        user0.id,
        Rating(5),
        "Amazing"
    ),
    Review(
        ReviewId.fromString("2c658b43-07f6-4b0e-aded-61a6595df277"),
        shop0.id,
        user2.id,
        Rating(1),
        "I didn't like it"
    ),
    Review(
        ReviewId.fromString("898f403f-3abb-4c98-a7bb-5ce2483e26b6"),
        shop0.id,
        user1.id,
        Rating(2),
        "ExampleReview"
    ),
    Review(
        ReviewId.fromString("64e80795-45a7-4972-ad11-8cd1cb298307"),
        shop0.id,
        user1.id,
        Rating(2),
        "ExampleReview"
    ),
    Review(
        ReviewId.fromString("d7b90437-f337-4f0b-9983-08f16f6dd70f"),
        shop0.id,
        user1.id,
        Rating(2),
        "ExampleReview"
    ),
    Review(
        ReviewId.fromString("add3fb4d-e2d8-4364-8ef5-ddc0c585349f"),
        shop0.id,
        user1.id,
        Rating(2),
        "ExampleReview"
    ),
    Review(
        ReviewId.fromString("7b4ad3ff-77d7-4bc8-8941-4a649886e9c9"),
        shop0.id,
        user1.id,
        Rating(2),
        "ExampleReview"
    ),
    Review(
        ReviewId.fromString("61f14112-5f1a-4385-aa0c-b9dd34aeb56e"),
        shop0.id,
        user1.id,
        Rating(2),
        "ExampleReview"
    ),
)

val shop1reviews = emptyList<Review>()

val shop2reviews = listOf(
    Review(
        ReviewId.fromString("37d40e5f-fa17-4c96-8fb6-4a8b38324403"),
        shop2.id,
        user0.id,
        Rating(5),
        "Amazing"
    ),
    Review(
        ReviewId.fromString("578c8b8c-6dac-452a-a901-de0fbdf0cf37"),
        shop2.id,
        user1.id,
        Rating(2),
        "Nuh, average"
    ),
)

val shop3reviews = listOf(
    Review(
        ReviewId.fromString("59a0d8c9-4257-4407-9674-8689fe2ca903"),
        shop3.id,
        user0.id,
        Rating(5),
        "Amazing"
    ),
    Review(
        ReviewId.fromString("f83e60d3-3296-4d90-ba6b-c424e890f00b"),
        shop3.id,
        user1.id,
        Rating(2),
        "Nuh, average"
    ),
)

val sampleReviews: List<Review> by lazy {
    listOf(shop0reviews, shop1reviews, shop2reviews, shop3reviews).flatten()
}

