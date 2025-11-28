package com.tondracek.myfarmer.core.repository.firestore

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class FirestoreCollection(val name: String)
