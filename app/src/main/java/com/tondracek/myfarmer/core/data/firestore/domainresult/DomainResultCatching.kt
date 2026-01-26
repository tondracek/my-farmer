package com.tondracek.myfarmer.core.data.firestore.domainresult

import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException
import kotlin.reflect.KClass

inline fun <T> domainResultOf(
    defaultError: DomainError,
    vararg errorMappings: Pair<KClass<out Throwable>, DomainError>,
    block: () -> T,
): DomainResult<T> = try {
    DomainResult.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Throwable) {
    val domainError = errorMappings
        .firstOrNull { (klass, _) -> klass.isInstance(e) }
        ?.second
        ?: defaultError

    DomainResult.Failure(domainError, e).also {
        Timber.e(e, "domainResultOf caught an exception, mapped to DomainError: $domainError")
    }
}

