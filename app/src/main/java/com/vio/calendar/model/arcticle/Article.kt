package com.vio.calendar.model.arcticle

import com.google.gson.annotations.SerializedName

data class Article (
    @SerializedName("_id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("short_description")
    val shortDescription: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("created_at")
    val creationDate: String,
    @SerializedName("picture_url")
    val image: String,
    @SerializedName("view_count")
    val likes: Int
)