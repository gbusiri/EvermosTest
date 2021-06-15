package com.mindorks.framework.mvi.data.api

import com.example.awesomeapp.data.model.CuratedResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

   @GET("curated")
   suspend fun retrieveImages(@Query("page") page: Int, @Query("per_page") limit: Int): CuratedResponse
}