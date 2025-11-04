package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.common.model.ImageResource
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.contactinfo.domain.model.MediaLink
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import java.util.UUID

val user0 = SystemUser(
    id = UUID.fromString("b620b542-7f8d-4b91-9a89-74307bacec32"),
    firebaseId = "ipOD0j66bTRv19aKjRJHB5regr43", // mail + mail
    name = "John Doe",
    profilePicture = ImageResource("https://picsum.photos/400/300"),
    contactInfo = ContactInfo(
        phoneNumber = "+1234567890",
        email = "john@doe.com",
        website = MediaLink("website", "example.com"),
        facebook = MediaLink("facebook", "fb.com/johndoe"),
        instagram = MediaLink("instagram", "insta.com/johndoe"),
    )
)

val user1 = SystemUser(
    id = UUID.fromString("c0766964-7b1f-4795-8b52-6953f07e1382"),
    firebaseId = "RUo7E7MbiPd4kDYnUFk1vAfekVo1", // mail + mail
    name = "Jane Smith",
    profilePicture = ImageResource("https://picsum.photos/400/300"),
    contactInfo = ContactInfo(
        phoneNumber = null,
        email = "jane.smith@email.com",
        website = null,
        facebook = null,
        instagram = null,
    )
)

val user2 = SystemUser(
    id = UUID.fromString("627986e6-89dc-4190-baeb-5811d82387a2"),
    firebaseId = "wS0LS2m5B5ZOitoKIxmDwcZNs4C3", //alice@longname.com + alice@longname.com
    name = "AliceLongname SuperJohnsonLong",
    profilePicture = ImageResource("https://picsum.photos/400/300"),
    contactInfo = ContactInfo(
        phoneNumber = null,
        email = null,
        website = null,
        facebook = null,
        instagram = null,
    )
)

val sampleUsers by lazyOf(listOf(user0, user1, user2))
