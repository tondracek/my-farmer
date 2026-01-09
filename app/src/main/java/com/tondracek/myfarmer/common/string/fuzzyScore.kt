package com.tondracek.myfarmer.common.string

fun fuzzyScore(query: String, target: String): Int {
    val q = normalize(query)
    val t = normalize(target)

    if (q.isBlank() || t.isBlank()) return 0

    var score = 0
    var ti = 0

    for (qc in q) {
        var found = false
        while (ti < t.length) {
            if (t[ti] == qc) {
                score += 10
                found = true
                ti++
                break
            }
            ti++
            score -= 1
        }
        if (!found) return 0
    }

    if (t.startsWith(q.first())) score += 5

    return score
}