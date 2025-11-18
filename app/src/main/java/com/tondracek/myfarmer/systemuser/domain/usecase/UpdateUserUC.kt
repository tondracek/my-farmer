package com.tondracek.myfarmer.systemuser.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import com.tondracek.myfarmer.auth.domain.usecase.result.NotLoggedInUCResult
import com.tondracek.myfarmer.common.usecase.result.UpdateFailedUCResult
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.systemuser.data.UserRepository
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import javax.inject.Inject

class UpdateUserUC @Inject constructor(
    private val repository: UserRepository,
    private val auth: FirebaseAuth
) {

    suspend operator fun invoke(item: SystemUser): UCResult<Unit> =
        runCatching {
            when (auth.currentUser?.uid == item.firebaseId) {
                true -> UCResult.Success(repository.update(item))
                false -> NotLoggedInUCResult()
            }
        }.getOrElse { UpdateFailedUCResult(it) }
}
