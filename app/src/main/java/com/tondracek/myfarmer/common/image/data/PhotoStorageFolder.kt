package com.tondracek.myfarmer.common.image.data

import com.tondracek.myfarmer.shop.domain.model.ShopId

sealed class PhotoStorageFolder(val path: String) {

    data object ProfilePictures : PhotoStorageFolder("profile_pictures")

    /**
     * @param shopId photos will be stored under folder with shopId name
     */
    data class ShopPhotos(internal val shopId: ShopId) : PhotoStorageFolder("shop_photos")
    data object None : PhotoStorageFolder("")

    open fun getPath(fileName: String): String = when (this) {
        is ProfilePictures -> "${this.path}/$fileName"
        is ShopPhotos -> "${this.path}/${this.shopId}/$fileName"
        is None -> fileName
    }
}