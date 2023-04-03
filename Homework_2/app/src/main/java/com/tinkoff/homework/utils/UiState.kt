package com.tinkoff.homework.utils

open class UiState<T> {

    open class Error<T>(
        val exception: Throwable?
    ) : UiState<T>()

    class Loading<T> : UiState<T>()

    class Data<T>(val data: T) : UiState<T>()

    class Init<T> : UiState<T>()
}
