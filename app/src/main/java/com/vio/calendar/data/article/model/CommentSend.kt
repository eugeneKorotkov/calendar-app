package com.vio.calendar.data.article.model

import com.google.gson.annotations.SerializedName

data class CommentSend (
    @SerializedName("content")
    val content: String
)