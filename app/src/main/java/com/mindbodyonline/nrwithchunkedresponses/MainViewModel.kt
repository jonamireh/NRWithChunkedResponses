package com.mindbodyonline.nrwithchunkedresponses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

class MainViewModel : ViewModel() {
    private val mockWebServer = MockWebServer()
    private val client = OkHttpClient()
        .newBuilder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    companion object {
        private const val MAX_CHUNK_SIZE_BYTES = 8
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mockWebServer.start()
        }
    }

    fun mockCall() {
        viewModelScope.launch(Dispatchers.IO) {
            mockWebServer.enqueue(MockResponse().apply {
                setChunkedBody("""{"error": "invalid_request"}""", MAX_CHUNK_SIZE_BYTES)
                setResponseCode(400)
            })

            client.newCall(
                Request.Builder()
                    .get()
                    .url(mockWebServer.url("/"))
                    .build()
            ).execute()
        }
    }

    fun realCall() {
        viewModelScope.launch(Dispatchers.IO) {
            client.newCall(
                // POST https://signin.mindbodyonline.com
                // No body or headers given on purpose
                Request.Builder()
                    .post(RequestBody.create(MediaType.get("application/json"), byteArrayOf()))
                    .url("https://signin.mindbodyonline.com/connect/token")
                    .build()
            ).execute()
        }
    }

    override fun onCleared() {
        super.onCleared()
        mockWebServer.shutdown()
    }
}