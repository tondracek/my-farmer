package com.tondracek.myfarmer.location.model

import android.content.Context
import com.tondracek.myfarmer.R

enum class DistanceUnit() {
    KM()
    ;

    fun toString(context: Context) = when (this) {
        KM -> context.getString(R.string.km)
    }
}