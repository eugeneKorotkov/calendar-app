package com.vio.calendar.data.article.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class Article (
    @PrimaryKey
    @SerializedName("_id")
    @Expose
    var id: String? = null,
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