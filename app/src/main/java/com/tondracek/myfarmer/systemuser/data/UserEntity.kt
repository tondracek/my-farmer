package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.contactinfo.data.ContactInfoEntity
import com.tondracek.myfarmer.contactinfo.data.toEntity
import com.tondracek.myfarmer.contactinfo.data.toModel
import com.tondracek.myfarmer.core.repository.firestore.FirestoreCollection
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@FirestoreCollection("user")
data class UserEntity(
    override var id: String = "",
    val firebaseId: String = "",
    val name: String = "",
    val profilePicture: String? = null,
    val contactInfo: ContactInfoEntity = ContactInfoEntity(),
) : FirestoreEntity

fun SystemUser.toEntity() = UserEntity(
    id = id.toString(),
    firebaseId = firebaseId,
    name = name,
    profilePicture = profilePicture.uri,
    contactInfo = contactInfo.toEntity()
)

fun UserEntity.toModel() = SystemUser(
    id = UUID.fromString(id),
    firebaseId = firebaseId,
    name = name,
    profilePicture = ImageResource(profilePicture),
    contactInfo = contactInfo.toModel()
)
