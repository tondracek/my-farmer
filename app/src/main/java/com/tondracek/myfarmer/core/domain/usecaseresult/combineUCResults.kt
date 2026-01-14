package com.tondracek.myfarmer.core.domain.usecaseresult

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

private fun checkResultsForFailure(
    vararg results: DomainResult<*>,
): DomainResult.Failure? {
    return results.firstOrNull { it is DomainResult.Failure } as DomainResult.Failure?
}

fun <R, T0, T1> combineUCResults(
    flow0: Flow<DomainResult<T0>>,
    flow1: Flow<DomainResult<T1>>,
    onFirstFailure: (DomainResult.Failure) -> R,
    transform: suspend (T0, T1) -> R,
): Flow<R> = combine(flow0, flow1) { uc0, uc1 ->
    val failure = checkResultsForFailure(uc0, uc1)

    when (failure == null) {
        false -> onFirstFailure(failure)
        true -> transform(
            (uc0 as DomainResult.Success<T0>).data,
            (uc1 as DomainResult.Success<T1>).data,
        )
    }
}

fun <R, T0, T1, T2> combineUCResults(
    flow0: Flow<DomainResult<T0>>,
    flow1: Flow<DomainResult<T1>>,
    flow2: Flow<DomainResult<T2>>,
    onFirstFailure: (DomainResult.Failure) -> R,
    transform: suspend (T0, T1, T2) -> R,
): Flow<R> = combine(flow0, flow1, flow2) { uc0, uc1, uc2 ->
    val failure = checkResultsForFailure(uc0, uc1, uc2)

    when (failure == null) {
        false -> onFirstFailure(failure)
        true -> transform(
            (uc0 as DomainResult.Success<T0>).data,
            (uc1 as DomainResult.Success<T1>).data,
            (uc2 as DomainResult.Success<T2>).data,
        )
    }
}

fun <R, T0, T1, T2, T3> combineUCResults(
    flow0: Flow<DomainResult<T0>>,
    flow1: Flow<DomainResult<T1>>,
    flow2: Flow<DomainResult<T2>>,
    flow3: Flow<DomainResult<T3>>,
    onFirstFailure: (DomainResult.Failure) -> R,
    transform: suspend (T0, T1, T2, T3) -> R,
): Flow<R> = combine(flow0, flow1, flow2, flow3) { uc0, uc1, uc2, uc3 ->
    val failure = checkResultsForFailure(uc0, uc1, uc2, uc3)

    when (failure == null) {
        false -> onFirstFailure(failure)
        true -> transform(
            (uc0 as DomainResult.Success<T0>).data,
            (uc1 as DomainResult.Success<T1>).data,
            (uc2 as DomainResult.Success<T2>).data,
            (uc3 as DomainResult.Success<T3>).data,
        )
    }
}

fun <R, T0, T1, T2, T3, T4> combineUCResults(
    flow0: Flow<DomainResult<T0>>,
    flow1: Flow<DomainResult<T1>>,
    flow2: Flow<DomainResult<T2>>,
    flow3: Flow<DomainResult<T3>>,
    flow4: Flow<DomainResult<T4>>,
    onFirstFailure: (DomainResult.Failure) -> R,
    transform: suspend (T0, T1, T2, T3, T4) -> R,
): Flow<R> = combine(flow0, flow1, flow2, flow3, flow4) { uc0, uc1, uc2, uc3, uc4 ->
    val failure = checkResultsForFailure(uc0, uc1, uc2, uc3, uc4)

    when (failure == null) {
        false -> onFirstFailure(failure)
        true -> transform(
            (uc0 as DomainResult.Success<T0>).data,
            (uc1 as DomainResult.Success<T1>).data,
            (uc2 as DomainResult.Success<T2>).data,
            (uc3 as DomainResult.Success<T3>).data,
            (uc4 as DomainResult.Success<T4>).data,
        )
    }
}
