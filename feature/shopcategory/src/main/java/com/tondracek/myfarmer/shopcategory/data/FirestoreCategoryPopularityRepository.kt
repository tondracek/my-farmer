package com.tondracek.myfarmer.shopcategory.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import com.tondracek.myfarmer.core.data.firestore.FirestoreCollectionNames
import com.tondracek.myfarmer.core.data.firestore.helpers.mapToDomains
import com.tondracek.myfarmer.core.data.toDomainResult
import com.tondracek.myfarmer.core.domain.domainerror.CategoryPopularityError
import com.tondracek.myfarmer.shopcategory.domain.repository.CategoryPopularityRepository
import javax.inject.Inject

class FirestoreCategoryPopularityRepository @Inject constructor() : CategoryPopularityRepository {

    private val firestore = Firebase.firestore
    private val collection = firestore.collection(FirestoreCollectionNames.CATEGORY_POPULARITY)

    override fun getMostPopularCategories(limit: Int) = collection
        .orderBy(CategoryPopularityEntity::count.name, Direction.DESCENDING)
        .whereGreaterThan(CategoryPopularityEntity::count.name, 0)
        .limit(limit.toLong())
        .snapshots()
        .mapToDomains(CategoryPopularityEntity::class) { it.toModel() }
        .toDomainResult(CategoryPopularityError.FetchingFailed)
}
