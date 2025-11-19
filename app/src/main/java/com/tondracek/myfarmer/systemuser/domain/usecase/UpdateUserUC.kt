package com.tondracek.myfarmer.systemuser.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import com.tondracek.myfarmer.auth.domain.usecase.result.NotLoggedInUCResult
import com.tondracek.myfarmer.common.image.data.PhotoStorage
import com.tondracek.myfarmer.common.image.data.PhotoStorageFolder
import com.tondracek.myfarmer.common.image.data.Quality
import com.tondracek.myfarmer.common.usecase.result.UpdateFailedUCResult
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.systemuser.data.UserRepository
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateUserUC @Inject constructor(
    private val repository: UserRepository,
    private val photoStorage: PhotoStorage,
    private val auth: FirebaseAuth
) {

    suspend operator fun invoke(item: SystemUser): UCResult<Unit> =
        runCatching { update(item) }
            .getOrElse { UpdateFailedUCResult(it) }

    private suspend fun update(item: SystemUser): UCResult<Unit> {
        if (auth.currentUser?.uid != item.firebaseId)
            return NotLoggedInUCResult()

        val original = repository.getById(item.id).first() ?: return UserNotFoundResult(item.id)
        val updateItem = item.loadNewPhoto(original = original)

        return UCResult.Success(repository.update(updateItem))
    }

    private suspend fun SystemUser.loadNewPhoto(original: SystemUser): SystemUser =
        when (original.profilePicture == this.profilePicture) {
            true -> this
            false -> {
                println("DELETING...")
                photoStorage.deletePhoto(original.profilePicture)
                println("UPLOADING...")
                photoStorage.uploadPhoto(
                    imageResource = this.profilePicture,
                    name = this.id.toString(),
                    folder = PhotoStorageFolder.PROFILE_PICTURES,
                    quality = Quality.HD
                ).let { this.copy(profilePicture = it) }
            }
        }
}
