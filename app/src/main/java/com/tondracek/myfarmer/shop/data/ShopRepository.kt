package com.tondracek.myfarmer.shop.data

import com.tondracek.myfarmer.core.repository.BaseRepository
import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.shop.domain.model.Shop
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(
    core: RepositoryCore<Shop>,
) : BaseRepository<Shop>(core)
