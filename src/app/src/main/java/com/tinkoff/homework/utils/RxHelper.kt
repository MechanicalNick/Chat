package com.tinkoff.homework.utils

import io.reactivex.Single

fun <T> List<Single<T>>.zipSingles(): Single<List<T>> {
    if (this.isEmpty()) return Single.just(emptyList())
    return Single.zip(this) {
        @Suppress("UNCHECKED_CAST")
        return@zip (it as Array<T>).toList()
    }
}