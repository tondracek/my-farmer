package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.core.repository.firestore.FirestoreCollection
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.serialization.Serializable

@Serializable
@FirestoreCollection("user")
data class UserEntity(
    override var id: String = "",
) : FirestoreEntity

fun SystemUser.toEntity() = UserEntity(
    id = id.toString(),
)

fun UserEntity.toModel() = SystemUser(
    id = java.util.UUID.fromString(id),
)