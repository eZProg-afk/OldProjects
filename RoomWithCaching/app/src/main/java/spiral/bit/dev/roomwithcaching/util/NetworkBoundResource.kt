package spiral.bit.dev.roomwithcaching.util

import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType> networkBoundResource(
     crossinline fetch: suspend () -> RequestType,
     crossinline query: () -> Flow<ResultType>,
     crossinline saveFetchResult: suspend (RequestType) -> Unit,
     crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()
    val flow = if (shouldFetch(data)) { // check if time to update the cache
        emit(Resource.Loading(data)) // display progress bar
        try {
            saveFetchResult(fetch()) // load
            query().map { Resource.Success(data = it) } // successfully loaded
        } catch (throwable: Throwable) {
            query().map { Resource.Error(throwable, data = it) } // error happened
        }
    } else { //if update cache is not necessary
        query().map { Resource.Success(data = it) }
    }

    emitAll(flow)
}