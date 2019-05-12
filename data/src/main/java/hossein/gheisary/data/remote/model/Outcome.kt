package hossein.gheisary.data.remote.model

import hossein.gheisary.data.remote.exception.NetworkException

sealed class Outcome<T> {
    data class Progress<T>(var loading: Boolean) : Outcome<T>()
    data class Success<T>(var data: T) : Outcome<T>()
    data class Failure<T>(val exception: NetworkException) : Outcome<T>()

    companion object {
        fun <T> loading(isLoading: Boolean): Outcome<T> = Progress(isLoading)

        fun <T> success(data: T): Outcome<T> = Success(data)

        fun <T> failure(exception: NetworkException): Outcome<T> = Failure(exception)
    }
}