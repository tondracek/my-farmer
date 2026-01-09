package com.tondracek.myfarmer.systemuser.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tondracek.myfarmer.core.firestore.FirestoreCollectionNames
import com.tondracek.myfarmer.core.firestore.helpers.firestoreGetById
import com.tondracek.myfarmer.core.firestore.helpers.firestoreGetByIds
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntityId
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import com.tondracek.myfarmer.systemuser.domain.port.GetUserByIdPort
import com.tondracek.myfarmer.systemuser.domain.port.GetUsersByIdsPort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreUserReadAdapter @Inject constructor(
) : GetUserByIdPort, GetUsersByIdsPort {

    private val firestore = Firebase.firestore
    private val collection = firestore.collection(FirestoreCollectionNames.USER)

    override fun invoke(id: UserId): Flow<SystemUser?> {
        val entityId = id.toFirestoreId()
        return firestoreGetById(collection, entityId, UserEntity::class)
            .map { it?.toModel() }
    }

    override fun invoke(ids: List<UserId>): Flow<List<SystemUser>> {
        val entityIds = ids.map { it.toFirestoreId() }
        return firestoreGetByIds(collection, entityIds, UserEntity::class)
            .map { entities -> entities.map { it.toModel() } }
    }
}

fun UserId.toFirestoreId(): FirestoreEntityId = this.toString()

private fun UserEntity.toModel() = UserEntityMapper().toModel(this)