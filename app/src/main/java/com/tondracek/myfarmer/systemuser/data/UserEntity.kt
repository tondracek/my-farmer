package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.core.repository.firebase.FirebaseEntity
import com.tondracek.myfarmer.core.repository.firebase.FirestoreCollection
import kotlinx.serialization.Serializable

@Serializable
@FirestoreCollection("user")
data class UserEntity(
    override var id: String = "",
) : FirebaseEntity