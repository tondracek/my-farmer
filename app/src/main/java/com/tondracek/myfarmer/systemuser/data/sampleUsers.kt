package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.model.UserId

val user0 = SystemUser(
    id = UserId.fromString("b620b542-7f8d-4b91-9a89-74307bacec32"),
    authId = AuthId("ipOD0j66bTRv19aKjRJHB5regr43"), // mail + mail
    name = "John Doe",
    profilePicture = ImageResource("https://picsum.photos/400/300"),
    contactInfo = ContactInfo(
        phoneNumber = "+1234567890",
        email = "john@doe.com",
        website = "example.com",
        facebookLink = "fb.com/johndoe",
        instagramLink = "instagram.com/johndoe",
    )
)

val user1 = SystemUser(
    id = UserId.fromString("c0766964-7b1f-4795-8b52-6953f07e1382"),
    authId = AuthId("RUo7E7MbiPd4kDYnUFk1vAfekVo1"), // mail + mail
    name = "Jane Smith",
    profilePicture = ImageResource("https://picsum.photos/400/300"),
    contactInfo = ContactInfo(
        phoneNumber = null,
        email = "jane.smith@email.com",
        website = null,
        facebookLink = null,
        instagramLink = null,
    )
)

val user2 = SystemUser(
    id = UserId.fromString("627986e6-89dc-4190-baeb-5811d82387a2"),
    authId = AuthId("wS0LS2m5B5ZOitoKIxmDwcZNs4C3"), //alice@longname.com + alice@longname.com
    name = "AliceLongname SuperJohnsonLong",
    profilePicture = ImageResource("https://picsum.photos/400/300"),
    contactInfo = ContactInfo(
        phoneNumber = null,
        email = null,
        website = null,
        facebookLink = null,
        instagramLink = null,
    )
)

val sampleUsers by lazyOf(listOf(user0, user1, user2))
