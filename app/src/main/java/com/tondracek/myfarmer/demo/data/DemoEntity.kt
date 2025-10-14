package com.tondracek.myfarmer.demo.data

import com.tondracek.myfarmer.core.repository.firebase.FirebaseEntity
import com.tondracek.myfarmer.core.repository.firebase.FirestoreCollection
import kotlinx.serialization.Serializable

@Serializable
@FirestoreCollection("demo")
data class DemoEntity(
    override var id: String = "",
    var name: String = "",
    var index: Int = 0,
    var date: String = "",
) : FirebaseEntity
