package com.tondracek.myfarmer.systemuser.domain.usecase

import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.common.image.data.PhotoStorage
import com.tondracek.myfarmer.common.image.data.PhotoStorageFolder
import com.tondracek.myfarmer.common.image.data.Quality
import com.tondracek.myfarmer.common.usecase.result.UpdateFailedUCResult
import com.tondracek.myfarmer.core.usecaseresult.ForbiddenUCResult
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.getOrReturn
import com.tondracek.myfarmer.systemuser.data.UserRepository
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateUserUC @Inject constructor(
    private val repository: UserRepository,
    private val photoStorage: PhotoStorage,
    private val getLoggedInUser: GetLoggedInUserUC,
) {

    suspend operator fun invoke(userToUpdate: SystemUser): UCResult<Unit> =
        UCResult.of(UpdateFailedUCResult()) {
            val currentUser = getLoggedInUser().first().getOrReturn { return it }
            if (currentUser.id != userToUpdate.id) return ForbiddenUCResult

            val original = repository.getById(userToUpdate.id).first()
                ?: return UserNotFoundResult(userToUpdate.id)
            val updateItem = userToUpdate.loadNewPhoto(original = original)

            repository.update(updateItem)
        }

    private suspend fun SystemUser.loadNewPhoto(original: SystemUser): SystemUser =
        when (original.profilePicture == this.profilePicture) {
            true -> this
            false -> {
                photoStorage.deletePhoto(original.profilePicture)
                photoStorage.uploadPhoto(
                    imageResource = this.profilePicture,
                    name = this.id.toString(),
                    folder = PhotoStorageFolder.ProfilePictures,
                    quality = Quality.HD
                ).let { this.copy(profilePicture = it) }
            }
        }
}
