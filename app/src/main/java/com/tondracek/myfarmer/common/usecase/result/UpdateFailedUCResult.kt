package com.tondracek.myfarmer.common.usecase.result

import com.tondracek.myfarmer.core.usecaseresult.UCResult

class UpdateFailedUCResult(throwable: Throwable? = null) :
    UCResult.Failure("Update operation failed", throwable?.message)