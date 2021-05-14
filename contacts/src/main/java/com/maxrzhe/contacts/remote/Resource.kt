package com.maxrzhe.contacts.remote

sealed class Resource<out T> {
    abstract val data: T?

    class Success<T>(override val data: T) : Resource<T>()

    class Loading<T> : Resource<T>() {
        override val data: T? get() = null
    }

    class Error<T>(val error: Throwable) : Resource<T>() {
        override val data: T? get() = null
    }
}