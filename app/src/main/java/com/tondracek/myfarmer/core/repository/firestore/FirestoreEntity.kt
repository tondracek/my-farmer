package com.tondracek.myfarmer.core.repository.firestore

import com.tondracek.myfarmer.core.repository.RepositoryEntity

interface FirestoreEntity : RepositoryEntity<String> {
    override var id: String
}
