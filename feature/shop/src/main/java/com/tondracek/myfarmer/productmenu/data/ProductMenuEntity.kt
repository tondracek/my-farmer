package com.tondracek.myfarmer.productmenu.data

import com.tondracek.myfarmer.productmenu.domain.model.MenuItem
import com.tondracek.myfarmer.productmenu.domain.model.MenuItemId
import com.tondracek.myfarmer.productmenu.domain.model.PriceLabel
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import kotlinx.serialization.Serializable

@Serializable
data class ProductMenuEntity(
    var items: List<MenuItemEntity> = emptyList(),
)

@Serializable
data class MenuItemEntity(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var price: PriceLabelEntity = PriceLabelEntity(),
    var inStock: Boolean = true,
)

@Serializable
data class PriceLabelEntity(
    var value: String = "",
)

fun ProductMenuEntity.toModel() = ProductMenu(
    items = items.map { itemEntity ->
        MenuItem(
            id = MenuItemId.fromString(itemEntity.id) ?: MenuItemId.new(),
            name = itemEntity.name,
            description = itemEntity.description,
            price = PriceLabel(value = itemEntity.price.value),
            inStock = itemEntity.inStock,
        )
    }
)

fun ProductMenu.toEntity() = ProductMenuEntity(
    items = items.map { menuItem ->
        MenuItemEntity(
            id = menuItem.id.toString(),
            name = menuItem.name,
            description = menuItem.description,
            price = PriceLabelEntity(value = menuItem.price.value),
            inStock = menuItem.inStock,
        )
    }
)