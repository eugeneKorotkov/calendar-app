package com.vio.calendar.data.user.model

import android.service.autofill.UserData
import com.google.gson.annotations.SerializedName

data class UserFetchRecord (
    @SerializedName("_id")
    var id: String,
    @SerializedName("login")
    var login: String,
    @SerializedName("hash")
    var hash: String,
    @SerializedName("token")
    var token: String,
    @SerializedName("data")
    var userData: UserData
)