package com.tondracek.myfarmer.image.data

sealed class PhotoStorageFolder(val path: String) {

    /**
     * @param userId as string. Photos will be stored under a folder with userId name
     */
    data class ProfilePictures(internal val userId: String) : PhotoStorageFolder("profile_pictures")

    /**
     * @param shopId as string. Photos will be stored under a folder with shopId name
     */
    data class ShopPhotos(internal val shopId: String) : PhotoStorageFolder("shop_photos")

    data object None : PhotoStorageFolder("")

    open fun getPath(fileName: String): String = when (this) {
        is ProfilePictures -> "${this.path}/${this.userId}/$fileName"
        is ShopPhotos -> "${this.path}/${this.shopId}/$fileName"
        is None -> fileName
    }
}