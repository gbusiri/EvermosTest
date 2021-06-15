package com.mindorks.framework.mvi.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val BASE_URL = "https://api.pexels.com/v1/"
    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().addInterceptor(AuthInterceptor()).build())
        .build()

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}
