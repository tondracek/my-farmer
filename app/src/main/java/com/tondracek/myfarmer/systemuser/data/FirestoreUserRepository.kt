package com.tondracek.myfarmer.systemuser.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.core.data.firestore.FirestoreCollectionNames
import com.tondracek.myfarmer.core.data.firestore.helpers.FirestoreCrudHelper
import com.tondracek.myfarmer.core.data.firestore.helpers.functions.firestoreGetByField
import com.tondracek.myfarmer.core.data.firestore.helpers.functions.firestoreGetByIds
import com.tondracek.myfarmer.core.domain.domainerror.UserError
import com.tondracek.myfarmer.core.domain.repository.firestore.FirestoreEntityId
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.core.domain.usecaseresult.toUCResult
import com.tondracek.myfarmer.core.domain.usecaseresult.toUCResultNonNull
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

    override suspend fun create(item: SystemUser) =
        DomainResult.of(UserError.CreationFailed) {
            helper.create(item.toEntity()).toUserId()
        }

    override suspend fun update(item: SystemUser) =
        DomainResult.of(UserError.UpdateFailed) {
            helper.update(item.toEntity())
        }

    override suspend fun delete(id: UserId) =
        DomainResult.of(UserError.DeletionFailed) {
            helper.delete(id.toFirestoreId())
        }

    override fun getById(id: UserId): Flow<DomainResult<SystemUser>> =
        helper.getById(id.toFirestoreId())
            .mapToModel()
            .toUCResultNonNull(UserError.NotFound, UserError.Unknown)

    override fun getAll(): Flow<DomainResult<List<SystemUser>>> =
        helper.getAll()
            .mapToModelList()
            .toUCResult(UserError.FetchingFailed)

    override fun getByIds(userIds: List<UserId>): Flow<DomainResult<List<SystemUser>>> =
        firestoreGetByIds(
            collection = collection,
            ids = userIds.map { it.toFirestoreId() },
            entityClass = UserEntity::class,
        )
            .mapToModelList()
            .toUCResult(UserError.FetchingFailed)

    override fun getUserByAuthId(authId: AuthId): Flow<DomainResult<SystemUser?>> =
        firestoreGetByField(
            collection = collection,
            entityClass = UserEntity::class,
            field = FieldPath.of(UserEntity::firebaseId.name),
            value = authId.value,
        )
            .map { it.firstOrNull() }
            .mapToModel()
            .toUCResult(UserError.FetchingFailed)

    private fun Flow<UserEntity?>.mapToModel(): Flow<SystemUser?> =
        this.map { entity -> entity?.toModel() }

    private fun Flow<List<UserEntity>>.mapToModelList(): Flow<List<SystemUser>> =
        this.map { entities -> entities.map { it.toModel() } }
}

fun FirestoreEntityId.toUserId() = UserId.fromString(this)
fun UserId.toFirestoreId() = this.value.toString()