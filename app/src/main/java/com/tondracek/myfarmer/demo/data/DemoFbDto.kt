package com.tondracek.myfarmer.demo.data

import com.tondracek.myfarmer.core.repository.firebase.FirebaseEntity
import com.tondracek.myfarmer.core.repository.firebase.FirestoreCollection
import kotlinx.serialization.Serializable

@Serializable
@FirestoreCollection("demo")
data class DemoFbDto(
    override val id: String,
    val nameeeeee: String,
    val index: Int,
    val date: String
) : FirebaseEntity
