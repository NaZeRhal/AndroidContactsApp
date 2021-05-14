package com.maxrzhe.contacts.remote

import kotlinx.coroutines.flow.*
import retrofit2.Response

inline fun <reified ResultType, reified RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> Resource<RequestType>,
    crossinline saveFetchResult: suspend (Resource<RequestType>) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true },
) = flow {
    val data: ResultType = query().first()
    if (shouldFetch(data)) {
        emit(Resource.Loading())
        val response = fetch()
        if (response is Resource.Success) {
            saveFetchResult(response)
            emitAll(query().map { Resource.Success(it) })
        } else if (response is Resource.Error) {
            emitAll(query().map { Resource.Error<ResultType>(response.error) })
        }
    } else {
        emitAll(query().map { Resource.Success(it) })
    }
}

suspend fun <T> getResponse(
    request: suspend () -> Response<T>,
    defaultErrorMessage: String
): Resource<T> {
    return try {
        val result = request.invoke()
        val resultData = result.body()
        if (result.isSuccessful && resultData != null) {
            Resource.Success(resultData)
        } else {
            Resource.Error(
                Throwable(result.errorBody()?.toString() ?: defaultErrorMessage)
            )
        }
    } catch (e: Throwable) {
        Resource.Error(e)
    }
}
