package com.tondracek.myfarmer.common.image.model

object FirebaseUrlCache {
    private const val MAX_SIZE = 100

    private val cache = object : LinkedHashMap<String, String>(MAX_SIZE, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, String>?): Boolean {
            return size > MAX_SIZE
        }
    }

    @Synchronized
    fun get(path: String): String? = cache[path]

    @Synchronized
    fun put(path: String, url: String) {
        cache[path] = url
    }
}