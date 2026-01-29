package com.tondracek.myfarmer.core.data.firestore

import com.tondracek.myfarmer.core.data.RepositoryEntity

typealias FirestoreEntityId = String

interface FirestoreEntity : RepositoryEntity<FirestoreEntityId> {
    override var id: FirestoreEntityId
}
