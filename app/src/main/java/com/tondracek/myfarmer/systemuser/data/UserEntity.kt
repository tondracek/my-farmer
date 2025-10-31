package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.common.model.ImageResource
import com.tondracek.myfarmer.core.repository.firestore.FirestoreCollection
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@FirestoreCollection("user")
data class UserEntity(
    override var id: String = "",
    val name: String = "",
    val profilePicture: String? = null,
) : FirestoreEntity

fun SystemUser.toEntity() = UserEntity(
    id = id.toString(),
    name = name,
    profilePicture = profilePicture.uri,
)

fun UserEntity.toModel() = SystemUser(
    id = UUID.fromString(id),
    name = name,
    profilePicture = ImageResource(profilePicture),
)