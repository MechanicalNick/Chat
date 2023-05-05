package com.tinkoff.homework

import android.util.Log
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockRequestDispatcher : Dispatcher() {

    private val responses: MutableMap<String, MockResponse> = mutableMapOf()

    override fun dispatch(request: RecordedRequest): MockResponse {
        request.path?.let { Log.e("path", it) }
        return responses[request.path] ?: MockResponse().setResponseCode(404)
    }

    fun returnsForPath(path: String, response: MockResponse.() -> MockResponse) {
        responses[path] = response(MockResponse())
    }
}
