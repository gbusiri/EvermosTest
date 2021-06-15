package com.example.awesomeapp.data.model

import com.google.gson.annotations.SerializedName

data class PhotoModel(
    @SerializedName("photographer")
    val photographer: String?,
    @SerializedName("photographer_url")
    val photographerUrl: String?,
    @SerializedName("src")
    val images: ImageModel? = null
)

data class ImageModel(
    @SerializedName("large")
    val imageLarge: String?,
    @SerializedName("tiny")
    val imageTiny: String?
)