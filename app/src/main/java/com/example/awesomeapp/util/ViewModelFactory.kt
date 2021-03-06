package com.example.awesomeapp.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.awesomeapp.ui.viewmodel.MainViewModel
import com.example.awesomeapp.data.api.ApiHelper
import com.example.awesomeapp.data.repository.RepositoryImpl

class ViewModelFactory(val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(RepositoryImpl(apiHelper)) as T
            }
            else -> throw IllegalArgumentException("Unknown class name")
        }
    }
}