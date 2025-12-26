package com.tondracek.myfarmer.core.repository.firestore.firestoreclient

import com.google.firebase.firestore.CollectionReference

interface FirestoreCollection : FirestoreQuery {
    fun document(id: String): FirestoreDocumentRef
}

class FirestoreCollectionImpl(
    val query: CollectionReference
) : FirestoreCollection, FirestoreQueryImpl(query) {

    override fun document(id: String): FirestoreDocumentRef =
        FirestoreDocumentRefImpl(query.document(id))
}