package com.tondracek.myfarmer.ui.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.domain.UseCaseResult
import com.tondracek.myfarmer.demo.domain.AddDemoUC
import com.tondracek.myfarmer.demo.domain.GetFilteredDemosUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DemoViewmodel @Inject constructor(
    private val addDemoUC: AddDemoUC,
    getFilteredDemos: GetFilteredDemosUC
) : ViewModel() {

    val state: StateFlow<DemoScreenState> = getFilteredDemos()
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


    fun addDemo() = viewModelScope.launch {
        addDemoUC()
    }
}
