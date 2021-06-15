package com.example.awesomeapp.data.model

import com.google.gson.annotations.SerializedName

data class CuratedResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("per_page")
    val limit: Int,
    @SerializedName("photos")
    val photos: List<PhotoModel> = listOf(),
    @SerializedName("next_page")
    val nextPageApi: String?
)