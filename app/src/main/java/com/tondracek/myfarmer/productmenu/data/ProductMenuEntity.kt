package com.tondracek.myfarmer.productmenu.data

import com.tondracek.myfarmer.productmenu.domain.model.MenuItem
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.productmenu.domain.model.ProductPrice
import kotlinx.serialization.Serializable

@Serializable
data class ProductMenuEntity(
    var items: List<MenuItemEntity> = emptyList(),
)

@Serializable
data class MenuItemEntity(
    var name: String = "",
    var description: String? = null,
    var price: ProductPriceEntity = ProductPriceEntity(),
    var inStock: Boolean = true,
)

@Serializable
data class ProductPriceEntity(
    var value: String = "",
)

fun ProductMenuEntity.toModel() = ProductMenu(
    items = items.map { itemEntity ->
        MenuItem(
            name = itemEntity.name,
            description = itemEntity.description,
            price = ProductPrice(value = itemEntity.price.value),
            inStock = itemEntity.inStock,
        )
    }
)

fun ProductMenu.toEntity() = ProductMenuEntity(
    items = items.map { menuItem ->
        MenuItemEntity(
            name = menuItem.name,
            description = menuItem.description,
            price = ProductPriceEntity(value = menuItem.price.value),
            inStock = menuItem.inStock,
        )
    }
)