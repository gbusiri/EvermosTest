package com.example.awesomeapp.ui.viewstate

import com.example.awesomeapp.data.model.CuratedResponse

sealed class MainState {
    object Idle : MainState()
    object CuratedLoading : MainState()
    data class Curated(val data: CuratedResponse) : MainState()
    data class Error(val message: String?) : MainState()
}