package com.example.awesomeapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.awesomeapp.R
import com.example.awesomeapp.data.model.PhotoModel
import com.example.awesomeapp.ui.adapter.CuratedAdapter
import com.example.awesomeapp.ui.intent.MainIntent
import com.example.awesomeapp.ui.viewmodel.MainViewModel
import com.example.awesomeapp.ui.viewstate.MainState
import com.example.awesomeapp.util.ViewModelFactory
import com.google.android.material.appbar.AppBarLayout
import com.mindorks.framework.mvi.data.api.ApiHelperImpl
import com.mindorks.framework.mvi.data.api.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private var currentPage = 0
    private var isGridView = false
    private var isLoading = true
    private var photos = mutableListOf<PhotoModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupViewModel()
        observeViewModel()
        setupParallax()
        fetchImages()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isGridView)
            menuInflater.inflate(R.menu.option_list, menu)
        else
            menuInflater.inflate(R.menu.option_grid, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.optionList -> {
                isGridView = false
            }
            R.id.optionGrid -> {
                isGridView = true
            }
        }
        invalidateOptionsMenu()
        setupRecyclerView()
        return true
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
                    is MainState.CuratedLoading -> {
                        isLoading = true
                        loading.visibility = View.VISIBLE
                    }
                    is MainState.Curated -> {
                        isLoading = false
                        loading.visibility = View.GONE
                        photos.addAll(it.data.photos)
                        setupRecyclerView()
                    }
                    is MainState.Error -> {
                        isLoading = false
                        loading.visibility = View.GONE
                        Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                    }
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

    private fun setupRecyclerView() {
        val curatedAdapter = CuratedAdapter(photos, isGridView) {
            goToDetailScreen()
        }
        recyclerView.apply {
            layoutManager = if (isGridView) StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                                else LinearLayoutManager(this@MainActivity)
            adapter = curatedAdapter
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastVisibleItem = if (isGridView) {
                    val lastItemPositions = (recyclerView.layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
                    if (lastItemPositions.isNotEmpty()) max(lastItemPositions[0], lastItemPositions[1]) else 0
                } else {
                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                }
                if (isLoading.not() && recyclerView.layoutManager?.itemCount == lastVisibleItem + 1) {
                    fetchImages()
                }
            }
        })
    }

    private fun goToDetailScreen() {
        Toast.makeText(this, "Hehe", Toast.LENGTH_SHORT).show()
    }
}