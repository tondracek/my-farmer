package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.contactinfo.data.ContactInfoEntity
import com.tondracek.myfarmer.contactinfo.data.toEntity
import com.tondracek.myfarmer.contactinfo.data.toModel
import com.tondracek.myfarmer.core.firestore.FirestoreCollectionNames
import com.tondracek.myfarmer.core.repository.firestore.FirestoreCollectionName
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.serialization.Serializable

@Serializable
@FirestoreCollectionName(FirestoreCollectionNames.USER)
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
