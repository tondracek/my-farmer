package com.tondracek.myfarmer.common.string

fun normalize(s: String): String =
    s.lowercase()
        .replace("ě", "e").replace("š", "s").replace("č", "c")
        .replace("ř", "r").replace("ž", "z").replace("ý", "y")
        .replace("á", "a").replace("í", "i").replace("é", "e")
        .replace("ú", "u").replace("ů", "u").replace("ó", "o")
