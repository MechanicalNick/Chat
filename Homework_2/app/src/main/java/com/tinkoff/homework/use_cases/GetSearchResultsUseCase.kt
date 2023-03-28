package com.tinkoff.homework.use_cases

import com.tinkoff.homework.data.dto.StreamRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.random.nextInt

object GetSearchResultsUseCase {

    suspend operator fun invoke(request: String): StreamRequest = withContext(Dispatchers.IO) {
        delay(Random.nextInt(500..2500).toLong())

        var random = Random.nextInt(0, 10)
        if(random == 9) {
            UseCaseStateHolder.hasError.set(true)
        }

        if (UseCaseStateHolder.hasError.get()) {
            UseCaseStateHolder.hasError.set(false)
            error("Some error occured")
        }

        if (request.isNotBlank()) {
            StreamRequest(FakeStreams.getFakeData().filter { streamDto ->
                streamDto.name.startsWith(request, true)
            })
        }
        else {
            StreamRequest(FakeStreams.getFakeData())
        }
    }
}