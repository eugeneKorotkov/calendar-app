package ru.korotkov.womencalendar.model.arcticle

import com.google.gson.annotations.SerializedName

data class Article (
    @SerializedName("_id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("short_description")
    val shortDescription: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("view_count")
    val likes: String
)