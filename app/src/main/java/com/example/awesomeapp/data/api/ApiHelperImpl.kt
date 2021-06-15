package com.mindorks.framework.mvi.data.api

import com.example.awesomeapp.data.model.CuratedResponse

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun retrieveImages(page: Int, limit: Int): CuratedResponse = apiService.retrieveImages(page, limit)
}