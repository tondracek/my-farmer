package com.tondracek.myfarmer.ui.core.logging

import timber.log.Timber

class FarmerDebugTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String {
        val originalTag = super.createStackElementTag(element)
        return "[FARMER] $originalTag"
    }
}
