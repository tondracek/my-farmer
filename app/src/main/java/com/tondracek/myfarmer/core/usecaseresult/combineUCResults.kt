package com.tondracek.myfarmer.core.usecaseresult

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

private fun checkResultsForFailure(
    vararg results: UCResult<*>
): UCResult.Failure? {
    results.forEach {
        if (it is UCResult.Failure) return it
    }
    return null
}

fun <R, T0, T1> combineUCResults(
    flow0: Flow<UCResult<T0>>,
    flow1: Flow<UCResult<T1>>,
    onFirstFailure: (UCResult.Failure) -> R,
    transform: (T0, T1) -> R
): Flow<R> = combine(flow0, flow1) { uc0, uc1 ->
    val failure = checkResultsForFailure(uc0, uc1)

    when (failure == null) {
        false -> onFirstFailure(failure)
        true -> transform(
            (uc0 as UCResult.Success<T0>).data,
            (uc1 as UCResult.Success<T1>).data,
        )
    }
}

fun <R, T0, T1, T2> combineUCResults(
    flow0: Flow<UCResult<T0>>,
    flow1: Flow<UCResult<T1>>,
    flow2: Flow<UCResult<T2>>,
    onFirstFailure: (UCResult.Failure) -> R,
    transform: (T0, T1, T2) -> R
): Flow<R> = combine(flow0, flow1, flow2) { uc0, uc1, uc2 ->
    val failure = checkResultsForFailure(uc0, uc1, uc2)

    when (failure == null) {
        false -> onFirstFailure(failure)
        true -> transform(
            (uc0 as UCResult.Success<T0>).data,
            (uc1 as UCResult.Success<T1>).data,
            (uc2 as UCResult.Success<T2>).data,
        )
    }
}

fun <R, T0, T1, T2, T3> combineUCResults(
    flow0: Flow<UCResult<T0>>,
    flow1: Flow<UCResult<T1>>,
    flow2: Flow<UCResult<T2>>,
    flow3: Flow<UCResult<T3>>,
    onFirstFailure: (UCResult.Failure) -> R,
    transform: (T0, T1, T2, T3) -> R
): Flow<R> = combine(flow0, flow1, flow2, flow3) { uc0, uc1, uc2, uc3 ->
    val failure = checkResultsForFailure(uc0, uc1, uc2, uc3)

    when (failure == null) {
        false -> onFirstFailure(failure)
        true -> transform(
            (uc0 as UCResult.Success<T0>).data,
            (uc1 as UCResult.Success<T1>).data,
            (uc2 as UCResult.Success<T2>).data,
            (uc3 as UCResult.Success<T3>).data,
        )
    }
}

fun <R, T0, T1, T2, T3, T4> combineUCResults(
    flow0: Flow<UCResult<T0>>,
    flow1: Flow<UCResult<T1>>,
    flow2: Flow<UCResult<T2>>,
    flow3: Flow<UCResult<T3>>,
    flow4: Flow<UCResult<T4>>,
    onFirstFailure: (UCResult.Failure) -> R,
    transform: (T0, T1, T2, T3, T4) -> R
): Flow<R> = combine(flow0, flow1, flow2, flow3, flow4) { uc0, uc1, uc2, uc3, uc4 ->
    val failure = checkResultsForFailure(uc0, uc1, uc2, uc3, uc4)

    when (failure == null) {
        false -> onFirstFailure(failure)
        true -> transform(
            (uc0 as UCResult.Success<T0>).data,
            (uc1 as UCResult.Success<T1>).data,
            (uc2 as UCResult.Success<T2>).data,
            (uc3 as UCResult.Success<T3>).data,
            (uc4 as UCResult.Success<T4>).data,
        )
    }
}
