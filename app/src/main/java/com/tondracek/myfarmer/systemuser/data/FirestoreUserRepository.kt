package com.tondracek.myfarmer.systemuser.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.core.data.FirestoreCollectionNames
import com.tondracek.myfarmer.core.firestore.helpers.FirestoreCrudHelper
import com.tondracek.myfarmer.core.firestore.helpers.functions.firestoreGetByIds
import com.tondracek.myfarmer.core.firestore.helpers.functions.firestoreGetWhereEqualTo
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import com.tondracek.myfarmer.systemuser.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FirestoreUserRepository @Inject constructor() : UserRepository {

    private val firestore: FirebaseFirestore = Firebase.firestore
    private val collection: CollectionReference =
        firestore.collection(FirestoreCollectionNames.USER)

    private val helper = FirestoreCrudHelper(
        collection = collection,
        entityClass = UserEntity::class,
    )

    override suspend fun create(item: SystemUser): UserId =
        helper.create(item.toEntity()).toUserId()

    override suspend fun update(item: SystemUser) =
        helper.update(item.toEntity())

    override suspend fun delete(id: UserId) =
        helper.delete(id.toString())

    override fun getById(id: UserId): Flow<SystemUser?> =
        helper.getById(id.toString()).mapToModel()

    override fun getAll(): Flow<List<SystemUser>> =
        helper.getAll().mapToModelList()

    override fun getByIds(userIds: List<UserId>): Flow<List<SystemUser>> =
        firestoreGetByIds(
            collection = collection,
            ids = userIds.map { it.toString() },
            entityClass = UserEntity::class,
        ).mapToModelList()


    override fun getUserByAuthId(authId: AuthId): Flow<SystemUser?> =
        firestoreGetWhereEqualTo(
            collection = collection,
            entityClass = UserEntity::class,
            property = UserEntity::firebaseId,
            value = authId.value,
        )
            .map { it.firstOrNull() }
            .mapToModel()
}

private fun Flow<UserEntity?>.mapToModel(): Flow<SystemUser?> =
    this.map { userEntity -> userEntity?.toModel() }

private fun Flow<List<UserEntity>>.mapToModelList(): Flow<List<SystemUser>> =
    this.map { userEntities ->
        userEntities.map { userEntity -> userEntity.toModel() }
    }

private fun String.toUserId() = UserId.fromString(this)