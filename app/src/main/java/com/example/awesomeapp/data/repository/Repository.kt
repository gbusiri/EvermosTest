package com.example.awesomeapp.data.repository

import com.example.awesomeapp.data.api.ApiHelper

class Repository(val apiHelper: ApiHelper) {
    suspend fun retrieveImages(page: Int, limit: Int) = apiHelper.retrieveImages(page, limit)
}