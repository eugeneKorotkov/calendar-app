package com.vio.calendar.data.article.model

import com.google.gson.annotations.SerializedName
import com.vio.calendar.data.user.model.UserData


data class Comment(
    @SerializedName("_id")
    var id: String,
    @SerializedName("user_data")
    var userData: UserData,
    @SerializedName("content")
    var content: String
)