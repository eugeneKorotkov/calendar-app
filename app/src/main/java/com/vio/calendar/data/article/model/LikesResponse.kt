package com.vio.calendar.data.article.model

import com.google.gson.annotations.SerializedName

data class LikesResponseItem (
    @SerializedName("user_id")
    var userId: String
)