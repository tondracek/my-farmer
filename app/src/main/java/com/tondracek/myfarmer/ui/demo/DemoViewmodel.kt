package com.tondracek.myfarmer.ui.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.domain.UseCaseResult
import com.tondracek.myfarmer.demo.domain.Demo
import com.tondracek.myfarmer.demo.domain.GetFilteredDemosUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface DemoScreenState {
    object Loading : DemoScreenState
    data class Success(val demos: List<Demo>) : DemoScreenState
    data class Error(val message: String) : DemoScreenState
}

@HiltViewModel
class DemoViewmodel @Inject constructor(
    addDemo: AddDemoUC,
    getFilteredDemos: GetFilteredDemosUC
) : ViewModel() {

    val demosResult = emptyFlow()

    val state: StateFlow<DemoScreenState> = demosResult
        .map { result ->
            when (result) {
                is UseCaseResult.Success -> DemoScreenState.Success(result.data)
                is UseCaseResult.Failure -> DemoScreenState.Error(result.userError)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DemoScreenState.Loading
        )


    fun addDemo() {
        addDemo()
    }
}