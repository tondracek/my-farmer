package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.contactinfo.data.ContactInfoEntity
import com.tondracek.myfarmer.contactinfo.data.toEntity
import com.tondracek.myfarmer.contactinfo.data.toModel
import com.tondracek.myfarmer.core.data.FirestoreCollectionNames
import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.firestore.FirestoreCollectionName
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.serialization.Serializable
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
@FirestoreCollectionName(FirestoreCollectionNames.USER)
data class UserEntity(
    override var id: String = "",
    val firebaseId: String = "",
    val name: String = "",
    val profilePicture: String? = null,
    val contactInfo: ContactInfoEntity = ContactInfoEntity(),
) : FirestoreEntity

@Singleton
class UserEntityMapper @Inject constructor() : EntityMapper<SystemUser, UserEntity> {
    override fun toEntity(model: SystemUser) =
        UserEntity(
            id = model.id.toString(),
            firebaseId = model.firebaseId,
            name = model.name,
            profilePicture = model.profilePicture.uri,
            contactInfo = model.contactInfo.toEntity()
        )

    override fun toModel(entity: UserEntity) = SystemUser(
        id = UUID.fromString(entity.id),
        firebaseId = entity.firebaseId,
        name = entity.name,
        profilePicture = ImageResource(entity.profilePicture),
        contactInfo = entity.contactInfo.toModel()
    )
}
