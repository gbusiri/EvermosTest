package com.example.awesomeapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.awesomeapp.data.api.ApiHelperImpl
import com.example.awesomeapp.data.api.ApiService
import com.example.awesomeapp.data.model.CuratedResponse
import com.example.awesomeapp.data.repository.RepositoryImpl
import com.example.awesomeapp.ui.intent.MainIntent
import com.example.awesomeapp.ui.viewmodel.MainViewModel
import com.example.awesomeapp.ui.viewstate.MainState
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @Mock
    lateinit var apiService: ApiService

    @Mock
    lateinit var observer: Observer<MainState>

    @Test
    fun `verify fetch image return success`() {
        runBlockingTest {
            `when`(apiService.retrieveImages(anyInt(), anyInt())).thenReturn(CuratedResponse(1, 10, listOf(), "anyString()"))
            val apiHelper = ApiHelperImpl(apiService)
            val repository = RepositoryImpl(apiHelper)
            val viewModel = MainViewModel(repository)
            viewModel.state.asLiveData().observeForever(observer)
            viewModel.userIntent.send(MainIntent.RetrieveImages)
            verify(observer).onChanged(MainState.Success(CuratedResponse(1, 10, listOf(), "anyString()")))
        }
    }

    @Test
    fun `verify fetch images return error`() {
        runBlockingTest {
            `when`(apiService.retrieveImages(anyInt(), anyInt())).thenThrow(RuntimeException())
            val apiHelper = ApiHelperImpl(apiService)
            val repository = RepositoryImpl(apiHelper)
            val viewModel = MainViewModel(repository)
            viewModel.state.asLiveData().observeForever(observer)
            viewModel.userIntent.send(MainIntent.RetrieveImages)
            verify(observer).onChanged(MainState.Error(null))
        }
    }
}
