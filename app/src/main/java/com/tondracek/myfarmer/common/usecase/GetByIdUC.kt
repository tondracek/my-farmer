package com.tondracek.myfarmer.common.usecase

import com.tondracek.myfarmer.common.usecase.result.NotFoundUCResult
import com.tondracek.myfarmer.core.repository.Repository
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class GetByIdUC<Model> @Inject constructor(
    private val repository: Repository<Model>,
) {

    operator fun invoke(id: UUID?): Flow<UCResult<Model>> = id?.let {
        repository.getById(id)
            .map {
                when (it) {
                    null -> NotFoundUCResult()
                    else -> UCResult.Success(it)
                }
            }
    } ?: flowOf(NotFoundUCResult())
}