package com.example.awesomeapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.awesomeapp.R
import com.example.awesomeapp.data.model.PhotoModel
import com.example.awesomeapp.ui.adapter.CuratedAdapter
import com.example.awesomeapp.ui.intent.MainIntent
import com.example.awesomeapp.ui.viewmodel.MainViewModel
import com.example.awesomeapp.ui.viewstate.MainState
import com.example.awesomeapp.util.Constants
import com.example.awesomeapp.util.ViewModelFactory
import com.google.android.material.appbar.AppBarLayout
import com.mindorks.framework.mvi.data.api.ApiHelperImpl
import com.mindorks.framework.mvi.data.api.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))
        setContentView(R.layout.activity_main)

        setupViewModel()
        observeViewModel()
        setupParallax()
        fetchImages()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService)
            )
        ).get(MainViewModel::class.java)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mainViewModel.state.collect {
                when (it) {
                    is MainState.Idle -> {} // do nothing
                    is MainState.CuratedLoading -> {}
                    is MainState.Curated -> {
                        setupAdapter(it.data.photos)
                    }
                    is MainState.Error -> {}
                }
            }
        }
    }

    private fun fetchImages() {
        currentPage++
        lifecycleScope.launch {
            mainViewModel.apply {
                page = currentPage
                userIntent.send(MainIntent.RetrieveImages)
            }
        }
    }

    private fun setupParallax() {
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
            when {
                // Collapsed
                abs(offset) >= appBarLayout.totalScrollRange -> {
                    collapsingToolbar.apply {
                        isTitleEnabled = true
                        title = getString(R.string.app_name)
                        setCollapsedTitleTextColor(getColor(R.color.white))
                    }
                }
                // Expanded
                offset == 0 -> {
                    collapsingToolbar.isTitleEnabled = false
                }
                // Between them
                else -> {
                    collapsingToolbar.isTitleEnabled = false
                }
            }
        })
    }

    private fun setupAdapter(photos: List<PhotoModel>) {
        val curatedAdapter = CuratedAdapter(photos) {
            goToDetailScreen()
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = curatedAdapter
        }
    }

    private fun goToDetailScreen() {
        Toast.makeText(this, "Hehe", Toast.LENGTH_SHORT).show()
    }
}