package com.example.awesomeapp.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.awesomeapp.R
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }

        val img = intent.extras?.getString(MainActivity.TAG_IMG_LARGE)
        val photoghraper = intent.extras?.getString((MainActivity.TAG_PHOTOGRAPHER))
        val photoghraperUrl = intent.extras?.getString((MainActivity.TAG_PHOTOGRAPHER_URL))

        toolbar.title = getString(R.string.title_detail, photoghraper)
        Glide.with(this)
            .load(img)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(imgLarge)
        tvPhotographer.text = photoghraper
        tvPhotographerUrl.text = photoghraperUrl
    }
}