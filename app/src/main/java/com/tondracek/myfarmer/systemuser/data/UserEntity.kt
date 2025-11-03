package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.common.model.ImageResource
import com.tondracek.myfarmer.core.repository.firestore.FirestoreCollection
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.systemuser.domain.model.ContactInfo
import com.tondracek.myfarmer.systemuser.domain.model.MediaLink
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@FirestoreCollection("user")
data class UserEntity(
    override var id: String = "",
    val name: String = "",
    val profilePicture: String? = null,
    val contactInfo: ContactInfoEntity = ContactInfoEntity(),
) : FirestoreEntity

fun SystemUser.toEntity() = UserEntity(
    id = id.toString(),
    name = name,
    profilePicture = profilePicture.uri,
    contactInfo = contactInfo.toEntity()
)

fun UserEntity.toModel() = SystemUser(
    id = UUID.fromString(id),
    name = name,
    profilePicture = ImageResource(profilePicture),
    contactInfo = contactInfo.toModel()
)

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