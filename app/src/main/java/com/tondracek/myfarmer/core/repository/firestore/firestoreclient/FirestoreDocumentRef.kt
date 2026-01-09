package com.tondracek.myfarmer.core.repository.firestore.firestoreclient

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

interface FirestoreDocumentRef {
    suspend fun set(data: Any)
    suspend fun delete()
    fun snapshots(): Flow<DocumentSnapshot>
}

class FirestoreDocumentRefImpl(
    private val ref: DocumentReference
) : FirestoreDocumentRef {

    override suspend fun set(data: Any) {
        ref.set(data).await()
    }

    override suspend fun delete() {
        ref.delete().await()
    }

    override fun snapshots(): Flow<DocumentSnapshot> = ref.snapshots()
}