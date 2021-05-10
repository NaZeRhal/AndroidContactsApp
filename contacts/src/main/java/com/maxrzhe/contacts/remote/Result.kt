package com.maxrzhe.contacts.remote

data class Result<out T>(
    val status: Status,
    val data: T?,
    val error: String?
) {
    companion object {
        fun <T> loading(): Result<T> =
            Result(Status.LOADING, null, null)

        fun <T> success(data: T?): Result<T> =
            Result(Status.SUCCESS, data, null)

        fun <T> error(error: String?): Result<T> =
            Result(Status.ERROR, null, error)
    }
}