package com.example.awesomeapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.awesomeapp.R
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))
        setContentView(R.layout.activity_main)

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
}