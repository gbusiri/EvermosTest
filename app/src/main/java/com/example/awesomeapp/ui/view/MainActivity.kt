package com.example.awesomeapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.awesomeapp.R
import com.example.awesomeapp.data.model.PhotoModel
import com.example.awesomeapp.ui.adapter.CuratedAdapter
import com.example.awesomeapp.ui.intent.MainIntent
import com.example.awesomeapp.ui.viewmodel.MainViewModel
import com.example.awesomeapp.ui.viewstate.MainState
import com.example.awesomeapp.util.ViewModelFactory
import com.google.android.material.appbar.AppBarLayout
import com.example.awesomeapp.data.api.ApiHelperImpl
import com.example.awesomeapp.data.api.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private var currentPage = 0
    private var isGridView = false
    private var isLoading = true
    private var photos = mutableListOf<PhotoModel?>()
    lateinit var curatedAdapter: CuratedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupRecyclerView()
        setupParallax()
        setupViewModel()
        observeViewModel()
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
                    is MainState.Loading -> {
                        curatedAdapter.notifyItemChanged(photos.size - 1)
                        imgError.visibility = View.GONE
                    }
                    is MainState.Success -> {
                        isLoading = false
                        photos = photos.filterNotNull().toMutableList()
                        photos.addAll(it.data.photos)
                        setupRecyclerView()
                        imgError.visibility = View.GONE
                    }
                    is MainState.Error -> {
                        isLoading = false
                        photos = photos.filterNotNull().toMutableList()
                        setupRecyclerView()
                        imgError.visibility = View.VISIBLE
                        Toast.makeText(this@MainActivity, getString(R.string.error_no_connection), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun fetchImages() {
        photos.add(null) // for loading
        currentPage++
        isLoading = true
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
        curatedAdapter = CuratedAdapter(photos, isGridView) { v, photoModel ->
            goToDetailScreen(photoModel)
        }
        recyclerView.apply {
            layoutManager = if (isGridView) StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                                else LinearLayoutManager(this@MainActivity)
            adapter = curatedAdapter
        }

        nestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (isLoading.not() && scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                fetchImages()
                recyclerView.smoothScrollToPosition(photos.size)
            }
        })
    }

    private fun goToDetailScreen(photoModel: PhotoModel) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(TAG_IMG_LARGE, photoModel.images?.imageLarge)
            putExtra(TAG_PHOTOGRAPHER, photoModel.photographer)
            putExtra(TAG_PHOTOGRAPHER_URL, photoModel.photographerUrl)
        }
        startActivity(intent)
    }

    companion object {
        const val TAG_IMG_LARGE = "tag_img_large"
        const val TAG_PHOTOGRAPHER = "tag_photographer"
        const val TAG_PHOTOGRAPHER_URL = "tag_photographer_url"
    }
}