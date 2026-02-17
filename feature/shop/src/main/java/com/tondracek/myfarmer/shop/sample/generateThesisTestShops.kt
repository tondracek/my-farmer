package com.tondracek.myfarmer.shop.sample

import com.google.firebase.firestore.FirebaseFirestore
import com.tondracek.myfarmer.common.color.RgbColor
import com.tondracek.myfarmer.common.color.fromArgb
import com.tondracek.myfarmer.location.domain.model.Location
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.MenuItem
import com.tondracek.myfarmer.productmenu.domain.model.PriceLabel
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.shop.data.toEntity
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.user.domain.model.UserId
import kotlinx.coroutines.tasks.await
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

suspend fun generateThesisTestShops() {

    val firestore = FirebaseFirestore.getInstance()
    val collection = firestore.collection("shop")

    val centerLat = 49.1951     // Brno center (change if needed)
    val centerLon = 16.6068
    val radiusMeters = 80_000.0
    val earthRadius = 6_371_000.0

    val random = Random(System.currentTimeMillis())
    val batch = firestore.batch()

    repeat(450) { index ->

        // ---- Uniform random point in circle ----
        val distance = radiusMeters * sqrt(random.nextDouble())
        val bearing = random.nextDouble() * 2 * Math.PI

        val lat1 = Math.toRadians(centerLat)
        val lon1 = Math.toRadians(centerLon)

        val lat2 = asin(
            sin(lat1) * cos(distance / earthRadius) +
                    cos(lat1) * sin(distance / earthRadius) * cos(bearing)
        )

        val lon2 = lon1 + atan2(
            sin(bearing) * sin(distance / earthRadius) * cos(lat1),
            cos(distance / earthRadius) - sin(lat1) * sin(lat2)
        )

        val finalLat = Math.toDegrees(lat2)
        val finalLon = Math.toDegrees(lon2)

        // ---- Create shop ----
        val shop = Shop(
            id = ShopId.newId(),
            name = "THESIS_GEN_$index",
            description = "Automatically generated shop for pagination testing.",
            ownerId = UserId.fromString("f34f60d6-e9cc-4c0f-8208-b053a7998ff8"),
            categories = listOf(
                ShopCategory(
                    name = "TestCategory",
                    color = RgbColor.fromArgb(0xFF336699.toInt())
                )
            ),
            images = emptyList(),
            menu = ProductMenu(
                listOf(
                    MenuItem(
                        name = "Test Product",
                        description = "Generated item",
                        price = PriceLabel("${100 + random.nextInt(400)} CZK"),
                        inStock = random.nextBoolean(),
                    )
                )
            ),
            location = Location(finalLat, finalLon),
            openingHours = OpeningHours.Message(
                message = "Generated for thesis testing"
            )
        )

        val docRef = collection.document(shop.id.toString())
        batch.set(docRef, shop.toEntity())
    }

    batch.commit().await()
}

suspend fun deleteThesisTestShops() {
    val firestore = FirebaseFirestore.getInstance()
    val collection = firestore.collection("shop")

    val snapshot = collection
        .whereGreaterThanOrEqualTo("name", "THESIS_GEN_")
        .whereLessThan("name", "THESIS_GEN_~")
        .get()
        .await()

    val batch = firestore.batch()
    snapshot.documents.forEach { batch.delete(it.reference) }
    batch.commit().await()
}
