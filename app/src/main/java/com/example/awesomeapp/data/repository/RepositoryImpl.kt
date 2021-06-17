package com.example.awesomeapp.data.repository

import com.example.awesomeapp.data.api.ApiHelper
import com.example.awesomeapp.ui.viewstate.MainState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RepositoryImpl(val apiHelper: ApiHelper) : Repository {

    private val _state = MutableStateFlow<MainState>(MainState.Idle)
    override val state: StateFlow<MainState>
        get() = _state

    override suspend fun retrieveImages(page: Int, limit: Int) {
        _state.value = MainState.Loading
        _state.value = try {
            MainState.Success(apiHelper.retrieveImages(page, limit))
        } catch(e: Exception) {
            MainState.Error(e.localizedMessage)
        }
    }

}