package com.tondracek.myfarmer.core.repository.firestore.firestoreclient

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


interface FirestoreClient {
    fun collection(path: String): FirestoreCollection
}

class FirestoreClientImpl : FirestoreClient {
    private val db = Firebase.firestore

    override fun collection(path: String): FirestoreCollection =
        FirestoreCollectionImpl(db.collection(path))
}
