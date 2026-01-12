package com.tondracek.myfarmer.core.domain.repository.firestore

import com.tondracek.myfarmer.core.domain.repository.RepositoryEntity

typealias FirestoreEntityId = String

interface FirestoreEntity : RepositoryEntity<FirestoreEntityId> {
    override var id: FirestoreEntityId
}
