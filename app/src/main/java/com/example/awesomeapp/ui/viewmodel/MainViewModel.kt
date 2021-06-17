package com.example.awesomeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.awesomeapp.data.repository.Repository
import com.example.awesomeapp.ui.intent.MainIntent
import com.example.awesomeapp.ui.viewstate.MainState
import com.example.awesomeapp.util.Constants
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = repository.state
    val state: StateFlow<MainState>
        get() = _state

    var page = 0
    var limit = Constants.LIMIT_PER_PAGE

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.RetrieveImages -> repository.retrieveImages(page, limit)
                }
            }
        }
    }
}