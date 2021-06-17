package com.example.awesomeapp.data.repository

import com.example.awesomeapp.data.api.ApiHelper
import com.example.awesomeapp.ui.viewstate.MainState
import kotlinx.coroutines.flow.StateFlow

interface Repository {
    val state: StateFlow<MainState>

    suspend fun retrieveImages(page: Int, limit: Int)
}