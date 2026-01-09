package com.tondracek.myfarmer.core.repository.firestore

import com.tondracek.myfarmer.core.repository.RepositoryEntity

typealias FirestoreEntityId = String

interface FirestoreEntity : RepositoryEntity<FirestoreEntityId> {
    override var id: FirestoreEntityId
}
