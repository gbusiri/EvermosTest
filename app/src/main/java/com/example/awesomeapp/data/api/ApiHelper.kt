package com.mindorks.framework.mvi.data.api

import com.example.awesomeapp.data.model.CuratedResponse

interface ApiHelper {
    suspend fun retrieveImages(page: Int, limit: Int): CuratedResponse
}