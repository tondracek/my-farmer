package com.tondracek.myfarmer.user.data

import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.contactinfo.data.ContactInfoEntity
import com.tondracek.myfarmer.contactinfo.data.toEntity
import com.tondracek.myfarmer.contactinfo.data.toModel
import com.tondracek.myfarmer.core.data.firestore.FirestoreEntity
import com.tondracek.myfarmer.image.model.ImageResource
import com.tondracek.myfarmer.user.domain.model.SystemUser
import com.tondracek.myfarmer.user.domain.model.UserId
import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    override var id: String = "",
    val firebaseId: String = "",
    val name: String = "",
    val profilePicture: String? = null,
    val contactInfo: ContactInfoEntity = ContactInfoEntity(),
) : FirestoreEntity

fun SystemUser.toEntity() = UserEntity(
    id = id.toString(),
    firebaseId = authId.value,
    name = name,
    profilePicture = profilePicture.uri,
    contactInfo = contactInfo.toEntity()
)

fun UserEntity.toModel() = SystemUser(
    id = UserId.fromString(id),
    authId = AuthId(firebaseId),
    name = name,
    profilePicture = ImageResource(profilePicture),
    contactInfo = contactInfo.toModel()
)
