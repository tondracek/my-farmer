package com.tondracek.myfarmer.shop.domain.usecase

//class GetAllShopsUC @Inject constructor(
//    private val shopRepository: ShopRepository,
//    private val applyFiltersUC: ApplyFiltersUC,
//) {
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    operator fun invoke(
//        filters: ShopFilters = ShopFilters.None
//    ): Flow<UCResult<List<Shop>>> =
//        shopRepository.getAll()
//            .flatMap { applyFiltersUC(shops = it, filters = filters) }
//}