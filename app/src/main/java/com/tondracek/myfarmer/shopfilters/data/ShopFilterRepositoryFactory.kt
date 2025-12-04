package com.tondracek.myfarmer.shopfilters.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopFilterRepositoryFactory @Inject constructor() {

    private val repos = mutableMapOf<String, ShopFilterRepository>()

    fun createOrGet(key: String): ShopFilterRepository {
        return repos.getOrPut(key) { ShopFilterRepository() }
    }
}
