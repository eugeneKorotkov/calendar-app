package com.vio.calendar.model.article

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "article_table")
data class Article (
    @PrimaryKey
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