package com.tondracek.myfarmer.common.util

import timber.log.Timber

fun setupTimberForTests() {
    Timber.uprootAll()
    Timber.plant(object : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // no-op for JVM tests
        }
    })
}
