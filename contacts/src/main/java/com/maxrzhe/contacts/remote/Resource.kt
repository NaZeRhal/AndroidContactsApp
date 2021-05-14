package com.maxrzhe.contacts.remote

sealed class Resource<out T> {
    abstract val data: T?

    sealed class Success<T> : Resource<T>() {
        data class Data<T>(override val data: T) : Success<T>()

        class Completed<T> : Success<T>() {
            override val data: T? get() = null
        }
    }

    class Loading<T> : Resource<T>() {
        override val data: T? get() = null
    }

    class Error<T>(val error: Throwable) : Resource<T>() {
        override val data: T? get() = null
    }
}