package com.mindorks.framework.mvi.data.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder().apply {
            addHeader("Authorization", "563492ad6f91700001000001cc8aa454f3324fa38037d3e3a25b194d")
        }
        return chain.proceed(requestBuilder.build())
    }
}