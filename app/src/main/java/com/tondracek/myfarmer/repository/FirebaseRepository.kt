package com.tondracek.myfarmer.repository

import com.tondracek.myfarmer.demo.domain.Demo
import com.tondracek.myfarmer.repository.request.RepositoryRequest
import java.util.UUID
import javax.inject.Inject

abstract class FirebaseRepository<T>(
) : Repository<T> {

    abstract val collectionName: String

//    val db = Firebase.firestore

    override suspend fun create(item: T): UUID {
        TODO("Not yet implemented")
    }

    override suspend fun update(item: T): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getByID(id: UUID): T? {
        TODO("Not yet implemented")
    }

    override suspend fun get(request: RepositoryRequest): List<T> {
        TODO("Not yet implemented")
    }
}

class DemoFirebaseRepository @Inject constructor() : FirebaseRepository<Demo>() {
    override val collectionName = "demos"
}