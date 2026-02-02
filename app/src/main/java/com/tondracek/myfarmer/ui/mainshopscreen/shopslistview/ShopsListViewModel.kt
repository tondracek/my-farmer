package com.tondracek.myfarmer.ui.mainshopscreen.shopslistview

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.domainresult.getOrElse
import com.tondracek.myfarmer.core.domain.domainresult.mapSuccess
import com.tondracek.myfarmer.core.domain.domainresult.withFailure
import com.tondracek.myfarmer.location.domain.model.Location
import com.tondracek.myfarmer.location.domain.model.meters
import com.tondracek.myfarmer.location.domain.usecase.GetUserApproximateLocationUC
import com.tondracek.myfarmer.location.domain.usecase.measureMapDistance
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.usecase.GetAverageRatingsByShopUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.DistancePagingCursor
import com.tondracek.myfarmer.shop.domain.usecase.GetAllShopsPaginatedUC
import com.tondracek.myfarmer.shop.domain.usecase.GetShopsByDistancePagedUC
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.shopfilters.domain.usecase.ApplyShopFiltersUC
import com.tondracek.myfarmer.shopfilters.domain.usecase.GetShopFiltersUC
import com.tondracek.myfarmer.ui.common.paging.getDomainResultPageFlow
import com.tondracek.myfarmer.ui.common.shop.filter.ShopFiltersRepositoryKeys
import com.tondracek.myfarmer.ui.core.viewmodel.BaseViewModel
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components.toListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopsListViewModel @Inject constructor(
    getAverageRatingsByShopUC: GetAverageRatingsByShopUC,
    getUserApproximateLocationUC: GetUserApproximateLocationUC,
    getShopFilters: GetShopFiltersUC,
    private val getAllShopsPaginated: GetAllShopsPaginatedUC,
    private val getShopsByDistancePaged: GetShopsByDistancePagedUC,
    private val applyFiltersUC: ApplyShopFiltersUC,
) : BaseViewModel<ShopsListViewEffect>() {

    private val filters: StateFlow<ShopFilters> =
        getShopFilters(ShopFiltersRepositoryKeys.MAIN_SHOPS_SCREEN)

    private val approximateLocation = getUserApproximateLocationUC(50.meters)

    private val _refreshTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private val refreshTrigger = _refreshTrigger.onStart { emit(Unit) }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val shops = combine(
        approximateLocation,
        refreshTrigger,
        filters,
    ) { location, _, filters -> location to filters }
        .flatMapLatest { (location, filters) ->
            when (location) {
                null -> getShopsGenericPageFlow(filters)
                else -> getShopsByDistancePageFlow(location, filters)
            }
        }
        .cachedIn(viewModelScope)

    private val averageRatings: Flow<Map<ShopId, Rating>> = getAverageRatingsByShopUC()
        .withFailure { emitEffect(ShopsListViewEffect.ShowError(it.error)) }
        .getOrElse(emptyMap())
        .distinctUntilChanged()

    private val shopsUiData = combine(
        shops,
        approximateLocation,
        averageRatings
    ) { shopsPagingData, userLocation, ratings ->
        shopsPagingData.map { shop ->
            val distance = measureMapDistance(shop.location, userLocation)
            val rating = ratings[shop.id] ?: Rating.ZERO
            shop.toListItem(distance, rating)
        }
    }

    val state: StateFlow<ShopsListViewState> = flowOf(shopsUiData)
        .map { shopsPaging ->
            ShopsListViewState.Success(shopsPaging = shopsPaging)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ShopsListViewState.Loading,
        )

    fun openShopDetail(shopId: ShopId) = viewModelScope.launch {
        emitEffect(ShopsListViewEffect.OpenShopDetail(shopId))
    }

    fun refreshShopsList() =
        viewModelScope.launch { _refreshTrigger.emit(Unit) }

    // ----------------
    // Paging flows
    // ----------------

    private fun getShopsGenericPageFlow(
        filters: ShopFilters
    ): Flow<PagingData<Shop>> = getDomainResultPageFlow<Shop, ShopId>(
        showError = { emitEffect(ShopsListViewEffect.ShowError(it)) }
    ) { pageSize, cursor ->
        getAllShopsPaginated(
            limit = pageSize,
            after = cursor,
        ).mapSuccess {
            val filtered = applyFiltersUC.sync(shops = it, filters = filters)
            val nextCursor = it.lastOrNull()?.id
            filtered to nextCursor
        }
    }

    private fun getShopsByDistancePageFlow(
        location: Location,
        filters: ShopFilters
    ): Flow<PagingData<Shop>> = getDomainResultPageFlow<Shop, DistancePagingCursor>(
        showError = { emitEffect(ShopsListViewEffect.ShowError(it)) },
    ) { pageSize, cursor ->
        getShopsByDistancePaged(
            center = location,
            pageSize = pageSize,
            cursor = cursor
        ).mapSuccess { (shops, nextCursor) ->
            val filtered = applyFiltersUC.sync(shops = shops, filters = filters)
            filtered to nextCursor
        }
    }
}

sealed interface ShopsListViewEffect {

    data class OpenShopDetail(val shopId: ShopId) : ShopsListViewEffect

    data class ShowError(val error: DomainError) : ShopsListViewEffect
}
