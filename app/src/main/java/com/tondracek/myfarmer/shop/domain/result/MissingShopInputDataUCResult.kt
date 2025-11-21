package com.tondracek.myfarmer.shop.domain.result

import com.tondracek.myfarmer.core.usecaseresult.UCResult

data object MissingShopInputDataUCResult : UCResult.Failure(
    userError = "Not all required data were filled in",
    systemError = "ShopInput is missing required data",
)