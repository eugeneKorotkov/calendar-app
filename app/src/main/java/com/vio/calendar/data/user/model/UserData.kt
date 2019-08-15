package com.vio.calendar.data.user.model

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("name")
    var name: String,
    @SerializedName("color")
    var color: Int
)