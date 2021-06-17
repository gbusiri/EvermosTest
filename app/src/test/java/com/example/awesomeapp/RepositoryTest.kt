package com.example.awesomeapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.awesomeapp.data.api.ApiHelperImpl
import com.example.awesomeapp.data.api.ApiService
import com.example.awesomeapp.data.model.CuratedResponse
import com.example.awesomeapp.data.repository.RepositoryImpl
import com.example.awesomeapp.ui.viewstate.MainState
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RepositoryTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @Mock
    lateinit var apiService: ApiService

    @Mock
    private lateinit var observer: Observer<MainState>

    @Captor
    private lateinit var captor: ArgumentCaptor<MainState>

    @Test
    fun `verify when fetch images success`() {
        val apiHelper = ApiHelperImpl(apiService)
        val repository = RepositoryImpl(apiHelper).apply {
            state.asLiveData().observeForever(observer)
        }
        runBlockingTest {
            `when`(apiService.retrieveImages(anyInt(), anyInt())).thenReturn(CuratedResponse(1, 10, listOf(), "anyString()"))
            repository.retrieveImages(1, 10)
        }
        try {
            verify(observer, times(3)).onChanged(captor.capture())
            verify(observer).onChanged(MainState.Idle)
            verify(observer).onChanged(MainState.Loading)
            verify(observer).onChanged(MainState.Success(CuratedResponse(1, 10, listOf(), "anyString()")))
        } finally {
            repository.state.asLiveData().removeObserver(observer)
        }
    }

    @Test
    fun `verify when fetch images error`() {
        val apiHelper = ApiHelperImpl(apiService)
        val repository = RepositoryImpl(apiHelper).apply {
            state.asLiveData().observeForever(observer)
        }
        runBlockingTest {
            `when`(apiService.retrieveImages(anyInt(), anyInt())).thenThrow(RuntimeException())
            repository.retrieveImages(1, 10)
        }
        try {
            verify(observer, times(3)).onChanged(captor.capture())
            verify(observer).onChanged(MainState.Idle)
            verify(observer).onChanged(MainState.Loading)
            verify(observer).onChanged(MainState.Error(null))
        } finally {
            repository.state.asLiveData().removeObserver(observer)
        }
    }
}