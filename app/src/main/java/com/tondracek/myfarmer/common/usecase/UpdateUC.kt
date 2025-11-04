package com.tondracek.myfarmer.common.usecase

import com.tondracek.myfarmer.core.repository.Repository
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import javax.inject.Inject

class UpdateUC<Model> @Inject constructor(
    private val repository: Repository<Model>,
) {

    suspend operator fun invoke(item: Model): UCResult<Boolean> = runCatching {
        UCResult.Success(repository.update(item))
    }.getOrElse { UpdateFailedUCResult(it) }
}

class UpdateFailedUCResult(throwable: Throwable) :
    UCResult.Failure("Update operation failed", throwable.message)