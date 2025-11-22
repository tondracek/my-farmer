package com.tondracek.myfarmer.common.usecase.result

import com.tondracek.myfarmer.core.usecaseresult.UCResult

class IdNotSetUCResult : UCResult.Failure(
    userError = "Id was not specified",
    systemError = "Id is null",
)