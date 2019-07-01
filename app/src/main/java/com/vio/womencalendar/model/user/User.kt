package com.vio.womencalendar.model.user

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login")
    val login: String = "",
    @SerializedName("pass")
    val pass: String = "",
    @SerializedName("data")
    val data: UserInfo = UserInfo()
)