package com.example.diagnal_assignment.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Content(
    val name: String,
    @SerializedName("poster-image")
    @Expose
    val poster_image: String
)