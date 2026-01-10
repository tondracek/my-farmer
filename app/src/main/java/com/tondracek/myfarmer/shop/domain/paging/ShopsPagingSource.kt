package com.tondracek.myfarmer.shop.domain.paging

import com.tondracek.myfarmer.common.paging.IdPagingSource
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId

class ShopsPagingSource(
    getData: suspend (limit: Int, after: ShopId?) -> List<Shop>
) : IdPagingSource<ShopId, Shop>(
    getDataKey = { shop -> shop.id },
    getData = getData,
)