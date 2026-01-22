package com.tondracek.myfarmer.ui.createshopflow.common

import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.location.model.Location
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.MenuItem
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory

sealed interface ShopFormEvent {

    /** basic data */
    data class UpdateName(val name: String) : ShopFormEvent
    data class UpdateDescription(val description: String) : ShopFormEvent

    /** location */
    data class UpdateLocation(val location: Location) : ShopFormEvent

    /** images */
    data class AddImage(val image: ImageResource) : ShopFormEvent
    data class RemoveImage(val image: ImageResource) : ShopFormEvent

    data class MoveImageLeft(val index: Int) : ShopFormEvent
    data class MoveImageRight(val index: Int) : ShopFormEvent

    /** opening hours */
    data class UpdateOpeningHours(val openingHours: OpeningHours) : ShopFormEvent

    /** categories */
    data class AddCategory(val category: ShopCategory) : ShopFormEvent
    data class RemoveCategory(val category: ShopCategory) : ShopFormEvent

    /** menu items */
    data class AddMenuItem(val item: MenuItem) : ShopFormEvent
    data class EditMenuItem(val item: MenuItem) : ShopFormEvent
    data class RemoveMenuItem(val item: MenuItem) : ShopFormEvent

    companion object {
        fun applyEvent(shopInput: ShopInput, event: ShopFormEvent) = when (event) {
            is UpdateName -> shopInput.copy(name = event.name)
            is UpdateDescription -> shopInput.copy(description = event.description)
            is UpdateLocation -> shopInput.copy(location = event.location)
            is AddImage -> shopInput.copy(images = shopInput.images + event.image)
            is RemoveImage -> shopInput.copy(images = shopInput.images - event.image)
            is MoveImageLeft -> {
                val index = event.index
                if (index <= 0 || index >= shopInput.images.size) {
                    shopInput
                } else {
                    shopInput.images.toMutableList()
                        .apply {
                            val temp = this[index - 1]
                            this[index - 1] = this[index]
                            this[index] = temp
                        }
                        .let { updatedImages -> shopInput.copy(images = updatedImages) }
                }
            }

            is MoveImageRight -> {
                val index = event.index
                if (index < 0 || index >= shopInput.images.size - 1) {
                    shopInput
                } else {
                    shopInput.images.toMutableList()
                        .apply {
                            val temp = this[index + 1]
                            this[index + 1] = this[index]
                            this[index] = temp
                        }
                        .let { updatedImages -> shopInput.copy(images = updatedImages) }
                }
            }

            is UpdateOpeningHours -> shopInput.copy(openingHours = event.openingHours)
            is AddCategory -> shopInput.copy(categories = shopInput.categories + event.category)
            is RemoveCategory -> shopInput.copy(categories = shopInput.categories - event.category)
            is AddMenuItem -> shopInput.copy(menu = shopInput.menu.copy(items = shopInput.menu.items + event.item))
            is EditMenuItem -> {
                val updatedItems = shopInput.menu.items.map {
                    if (it.id == event.item.id) event.item else it
                }
                shopInput.copy(menu = shopInput.menu.copy(items = updatedItems))
            }

            is RemoveMenuItem -> {
                val updatedItems = shopInput.menu.items.filter { it.id != event.item.id }
                shopInput.copy(menu = shopInput.menu.copy(items = updatedItems))
            }
        }
    }
}