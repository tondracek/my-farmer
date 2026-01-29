package com.tondracek.myfarmer.ui.common.map.mapbox

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.circleLayer
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.tondracek.myfarmer.ui.common.color.contrastColor
import com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview.ShopMapItem


const val SHOP_SOURCE_ID = "shops-source"

const val SHOP_LAYER_ID = "shop-points"
const val CLUSTER_LAYER_ID = "clusters"
const val CLUSTER_COUNT_LAYER_ID = "cluster-count"

const val PROP_ID = "id"
const val PROP_ICON = "icon"
const val PROP_POINT_COUNT = "point_count"

fun createClusterLayer(color: Color) = circleLayer(CLUSTER_LAYER_ID, SHOP_SOURCE_ID) {
    filter(Expression.has(PROP_POINT_COUNT))
    circleRadius(18.0)
    circleColor(color.toArgb())
    circleBlur(0.2)
    circleOpacity(0.95)
}

fun createCountLayer(color: Color) = symbolLayer(CLUSTER_COUNT_LAYER_ID, SHOP_SOURCE_ID) {
    filter(Expression.has(PROP_POINT_COUNT))
    textField(Expression.get(PROP_POINT_COUNT))
    textSize(12.0)

    textColor(color.toArgb())
}

fun createShopLayer() = symbolLayer(SHOP_LAYER_ID, SHOP_SOURCE_ID) {
    filter(Expression.not(Expression.has(PROP_POINT_COUNT)))
    iconImage(Expression.get(PROP_ICON))
    iconAllowOverlap(true)
    iconIgnorePlacement(true)
    iconAnchor(IconAnchor.BOTTOM)
}

fun addShopLayers(
    style: Style,
    shops: Collection<ShopMapItem>,
    clusterColor: Color,
) {
    val source = geoJsonSource(SHOP_SOURCE_ID) {
        featureCollection(shopsToFeatureCollection(shops))
        cluster(true)
        clusterRadius(50)
        clusterMaxZoom(14)
    }

    style.addSource(source)

    style.addLayer(createClusterLayer(clusterColor))
    style.addLayer(createCountLayer(contrastColor(clusterColor)))
    style.addLayer(createShopLayer())
}
