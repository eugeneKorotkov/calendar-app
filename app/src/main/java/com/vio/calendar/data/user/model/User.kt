package com.vio.calendar.data.user.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login")
    var name: String,
    @SerializedName("pass")
    var pass: String,
    @SerializedName("data")
    var userData: UserData
)