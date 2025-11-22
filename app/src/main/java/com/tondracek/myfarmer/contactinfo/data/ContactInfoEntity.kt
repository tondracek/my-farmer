package com.tondracek.myfarmer.contactinfo.data

import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import kotlinx.serialization.Serializable

@Serializable
data class ContactInfoEntity(
    val phoneNumber: String? = null,
    val email: String? = null,
    val website: String? = null,
    val facebook: String? = null,
    val instagram: String? = null,
)

fun ContactInfo.toEntity() = ContactInfoEntity(
    phoneNumber = phoneNumber,
    email = email,
    website = website,
    facebook = facebookLink,
    instagram = instagramLink,
)

fun ContactInfoEntity.toModel() = ContactInfo(
    phoneNumber = phoneNumber,
    email = email,
    website = website,
    facebookLink = facebook,
    instagramLink = instagram,
)
