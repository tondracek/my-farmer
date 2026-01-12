package com.tondracek.myfarmer.ui.common.map.mapbox

import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview.ShopMapItem

fun ImageResource.toStyleIconId(): String =
    "shop-icon-${this.uri}"

fun shopsToFeatureCollection(shops: Collection<ShopMapItem>): FeatureCollection {
    return FeatureCollection.fromFeatures(
        shops.map { shop ->
            val iconId = shop.icon.toStyleIconId()

            Feature.fromGeometry(
                Point.fromLngLat(
                    shop.location.longitude,
                    shop.location.latitude
                )
            ).apply {
                addStringProperty(PROP_ID, shop.id.toString())
                addStringProperty(PROP_ICON, iconId)
            }
        }
    )
}
