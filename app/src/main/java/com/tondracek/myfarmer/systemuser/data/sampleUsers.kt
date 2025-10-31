package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.common.model.ImageResource
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import java.util.UUID

val user0 = SystemUser(
    id = UUID.fromString("b620b542-7f8d-4b91-9a89-74307bacec32"),
    name = "John Doe",
    profilePicture = ImageResource("https://picsum.photos/400/300"),
)
val user1 = SystemUser(
    id = UUID.fromString("c0766964-7b1f-4795-8b52-6953f07e1382"),
    name = "Jane Smith",
    profilePicture = ImageResource("https://picsum.photos/400/300")
)
val user2 = SystemUser(
    id = UUID.fromString("627986e6-89dc-4190-baeb-5811d82387a2"),
    name = "AliceLongname SuperJohnsonLong",
    profilePicture = ImageResource("https://picsum.photos/400/300")
)

val sampleUsers by lazyOf(listOf(user0, user1, user2))
