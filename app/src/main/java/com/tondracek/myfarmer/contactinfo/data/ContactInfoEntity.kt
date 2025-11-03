package com.tondracek.myfarmer.contactinfo.data

import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.contactinfo.domain.model.MediaLink
import kotlinx.serialization.Serializable

@Serializable
data class ContactInfoEntity(
    val phoneNumber: String? = null,
    val email: String? = null,
    val website: MediaLinkEntity? = null,
    val facebook: MediaLinkEntity? = null,
    val instagram: MediaLinkEntity? = null,
)

fun ContactInfo.toEntity() = ContactInfoEntity(
    phoneNumber = phoneNumber,
    email = email,
    website = website?.toEntity(),
    facebook = facebook?.toEntity(),
    instagram = instagram?.toEntity(),
)

fun ContactInfoEntity.toModel() = ContactInfo(
    phoneNumber = phoneNumber,
    email = email,
    website = website?.toModel(),
    facebook = facebook?.toModel(),
    instagram = instagram?.toModel(),
)

@Serializable
data class MediaLinkEntity(
    val title: String = "",
    val url: String = "",
)

fun MediaLink.toEntity() = MediaLinkEntity(
    title = title,
    url = url,
)

fun MediaLinkEntity.toModel() = MediaLink(
    title = title,
    url = url,
)