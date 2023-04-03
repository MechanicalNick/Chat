package com.tinkoff.homework.use_cases

import java.util.concurrent.atomic.AtomicBoolean

object UseCaseStateHolder {

    val hasError: AtomicBoolean = AtomicBoolean(false)
}