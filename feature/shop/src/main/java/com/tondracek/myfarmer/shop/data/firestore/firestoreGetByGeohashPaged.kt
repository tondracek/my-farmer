package com.tondracek.myfarmer.shop.data.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.tondracek.myfarmer.core.data.firestore.helpers.toObjectsWithId
import com.tondracek.myfarmer.location.data.geohash.GeoHashRange
import com.tondracek.myfarmer.shop.data.entity.ShopEntity
import kotlinx.coroutines.tasks.await

suspend fun firestoreGetByGeohashPaged(
    collection: CollectionReference,
    range: GeoHashRange,
    limit: Int,
    afterGeohash: String?,
): List<ShopEntity> =
    collection
        .orderBy("location.geohash")
        .orderBy(FieldPath.documentId())
        .whereGreaterThanOrEqualTo("location.geohash", range.start)
        .whereLessThanOrEqualTo("location.geohash", range.end)
        .startAfterNullable(afterGeohash)
        .limit(limit.toLong())
        .get()
        .await()
        .toObjectsWithId(ShopEntity::class)

private fun Query.startAfterNullable(afterGeohash: String?): Query =
    afterGeohash?.let { id -> this.startAfter(id) } ?: this
