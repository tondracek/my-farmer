package com.tondracek.myfarmer.core.repository.firebase

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class FirestoreCollection(val name: String)
