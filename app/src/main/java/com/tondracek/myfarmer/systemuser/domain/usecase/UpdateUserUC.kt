package com.tondracek.myfarmer.systemuser.domain.usecase

import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.common.image.data.PhotoStorage
import com.tondracek.myfarmer.common.image.data.PhotoStorageFolder
import com.tondracek.myfarmer.common.image.data.Quality
import com.tondracek.myfarmer.core.domain.domainerror.AuthError
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.core.domain.usecaseresult.getOrReturn
import com.tondracek.myfarmer.core.domain.usecaseresult.mapFlatten
import com.tondracek.myfarmer.core.domain.usecaseresult.mapSuccess
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateUserUC @Inject constructor(
    private val repository: UserRepository,
    private val photoStorage: PhotoStorage,
    private val getLoggedInUser: GetLoggedInUserUC,
) {

    suspend operator fun invoke(userToUpdate: SystemUser): UCResult<Unit> {
        val currentUser = getLoggedInUser().first()
            .getOrReturn { return it }
        if (currentUser.id != userToUpdate.id)
            return UCResult.Failure(AuthError.Forbidden)

        val original = repository.getById(userToUpdate.id).first()
            .getOrReturn { return it }

        return userToUpdate.loadNewPhoto(original = original)
            .mapFlatten { repository.update(it) }
    }

    private suspend fun SystemUser.loadNewPhoto(original: SystemUser): UCResult<SystemUser> =
        when (original.profilePicture == this.profilePicture) {
            true -> UCResult.Success(this)
            false ->
                photoStorage.deletePhoto(original.profilePicture)
                    .mapFlatten {
                        photoStorage.uploadPhoto(
                            imageResource = this.profilePicture,
                            name = this.id.toString(),
                            folder = PhotoStorageFolder.ProfilePictures,
                            quality = Quality.HD
                        ).mapSuccess { this.copy(profilePicture = it) }
                    }
        }
}
