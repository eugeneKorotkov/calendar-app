package com.vio.calendar.model.arcticle

import com.google.gson.annotations.SerializedName

data class LikesCount(
    @SerializedName("count")
    val count: Int
)