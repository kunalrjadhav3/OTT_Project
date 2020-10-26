package com.example.diagnal_assignment.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

 class Page(
    @SerializedName("content-items")
    @Expose
    val content_items: ContentItems,
    @SerializedName("page-num")
    @Expose
    val page_num: String,
    @SerializedName("page-size")
    @Expose
    val page_size: String,

    val title: String,
    @SerializedName("total-content-items")
    @Expose
    val total_content_items: String
)