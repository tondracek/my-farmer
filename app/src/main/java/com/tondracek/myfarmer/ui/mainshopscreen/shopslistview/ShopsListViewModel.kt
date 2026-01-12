package com.tondracek.myfarmer.ui.mainshopscreen.shopslistview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import androidx.paging.map
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.getOrElse
import com.tondracek.myfarmer.core.usecaseresult.toException
import com.tondracek.myfarmer.location.model.Location
import com.tondracek.myfarmer.location.model.meters
import com.tondracek.myfarmer.location.usecase.measureMapDistance
import com.tondracek.myfarmer.location.usecase.measureMapDistanceNotNull
import com.tondracek.myfarmer.map.GetUserLocationUC
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.usecase.GetAverageRatingsByShopUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.DistancePagingCursor
import com.tondracek.myfarmer.shop.domain.usecase.GetAllShopsPaginatedUC
import com.tondracek.myfarmer.shop.domain.usecase.GetShopsByDistancePagedUC
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.shopfilters.domain.usecase.ApplyFiltersUC
import com.tondracek.myfarmer.shopfilters.domain.usecase.GetShopFiltersUC
import com.tondracek.myfarmer.ui.common.paging.getUCResultPageFlow
import com.tondracek.myfarmer.ui.common.shop.filter.ShopFiltersRepositoryKeys
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components.ShopListViewItem
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components.toListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopsListViewModel @Inject constructor(
    getAverageRatingsByShopUC: GetAverageRatingsByShopUC,
    getUserLocation: GetUserLocationUC,
    getAllShopsPaginated: GetAllShopsPaginatedUC,
    getShopFilters: GetShopFiltersUC,
    getShopsByDistancePaged: GetShopsByDistancePagedUC,
    applyFiltersUC: ApplyFiltersUC,
) : ViewModel() {

    private val filters: StateFlow<ShopFilters> =
        getShopFilters(ShopFiltersRepositoryKeys.MAIN_SHOPS_SCREEN)

    private val userLocation: Flow<Location?> = getUserLocation()
        .distinctUntilChanged()
        .getOrElse(null) { _effects.emit(ShopsListViewEffect.ShowError(it.userError)) }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val shops: Flow<PagingData<Shop>> = userLocation
        .scan<Location?, Location?>(null) { lastAccepted, current ->
            if (lastAccepted == null) return@scan current
            if (current == null) return@scan lastAccepted

            val d = measureMapDistanceNotNull(lastAccepted, current)

            if (d > 50.meters) current else lastAccepted
        }
        .distinctUntilChanged()
        .combine(filters) { location, filters -> location to filters }
        .flatMapLatest { (location, filters) ->
            when (location) {
                null -> getUCResultPageFlow(
                    getDataKey = { shop: Shop -> shop.id },
                    pageSize = 20,
                    getData = { limit, after ->
                        getAllShopsPaginated(
                            limit = limit,
                            after = after
                        ).mapSuccess {
                            applyFiltersUC.sync(
                                shops = it,
                                filters = filters,
                            )
                        }
                    }
                )

                else -> getShopsByDistancePageFlow(
                    getShops = { center, pageSize, cursor ->
                        getShopsByDistancePaged(
                            center = center,
                            pageSize = pageSize,
                            cursor = cursor
                        ).mapSuccess { (shops, nextCursor) ->
                            val filteredShops = applyFiltersUC.sync(
                                shops = shops,
                                filters = filters,
                            )
                            filteredShops to nextCursor
                        }
                    },
                    center = location,
                    pageSize = 20,
                )
            }
        }.cachedIn(viewModelScope)

    private val averageRatings: Flow<Map<ShopId, Rating>> = getAverageRatingsByShopUC()
        .getOrElse(emptyMap()) { _effects.emit(ShopsListViewEffect.ShowError(it.userError)) }
        .distinctUntilChanged()

    val shopsUiData: Flow<PagingData<ShopListViewItem>> = combine(
        shops,
        userLocation,
        averageRatings
    ) { shopsPagingData, userLocation, ratings ->
        shopsPagingData.map { shop ->
            val distance = measureMapDistance(shop.location, userLocation)
            val rating = ratings[shop.id] ?: Rating.ZERO
            shop.toListItem(distance, rating)
        }
    }

    private val _effects = MutableSharedFlow<ShopsListViewEffect>(extraBufferCapacity = 1)
    val effects: SharedFlow<ShopsListViewEffect> = _effects

    fun openShopDetail(shopId: ShopId) = viewModelScope.launch {
        _effects.emit(ShopsListViewEffect.OpenShopDetail(shopId))
    }

    fun onBackClicked() = viewModelScope.launch {
        _effects.emit(ShopsListViewEffect.OnBackClicked)
    }
}

sealed interface ShopsListViewEffect {
    data class OpenShopDetail(val shopId: ShopId) : ShopsListViewEffect

    data object OnBackClicked : ShopsListViewEffect

    data class ShowError(val message: String) : ShopsListViewEffect
}

fun getShopsByDistancePageFlow(
    getShops: suspend (center: Location, pageSize: Int, cursor: DistancePagingCursor?) -> UCResult<Pair<List<Shop>, DistancePagingCursor?>>,
    center: Location,
    pageSize: Int,
): Flow<PagingData<Shop>> = Pager(
    config = PagingConfig(
        pageSize = pageSize,
        enablePlaceholders = false,
    ),
    pagingSourceFactory = {
        ShopsByDistancePagingSource(
            getShops = getShops,
            center = center,
            pageSize = pageSize,
        )
    }
).flow


class ShopsByDistancePagingSource(
    private val getShops: suspend (center: Location, pageSize: Int, cursor: DistancePagingCursor?) -> UCResult<Pair<List<Shop>, DistancePagingCursor?>>,
    private val center: Location,
    private val pageSize: Int,
) : PagingSource<DistancePagingCursor, Shop>() {

    override suspend fun load(
        params: LoadParams<DistancePagingCursor>
    ): LoadResult<DistancePagingCursor, Shop> {
        val cursor = params.key

        val result = getShops(center, pageSize, cursor)

        return when (result) {
            is UCResult.Success<Pair<List<Shop>, DistancePagingCursor?>> -> {
                val (shops, nextCursor) = result.data
                LoadResult.Page(
                    data = shops,
                    prevKey = null,
                    nextKey = nextCursor,
                )
            }

            is UCResult.Failure -> LoadResult.Error(result.toException())
        }
    }


    override fun getRefreshKey(
        state: PagingState<DistancePagingCursor, Shop>
    ): DistancePagingCursor? {
        // Always restart from the beginning
        return null
    }
}
