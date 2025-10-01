package com.tondracek.myfarmer.ui.demo

import com.tondracek.myfarmer.demo.domain.Demo

sealed interface DemoScreenState {
    object Loading : DemoScreenState
    data class Success(val demos: List<Demo>) : DemoScreenState
    data class Error(val message: String) : DemoScreenState
}
