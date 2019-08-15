package com.vio.calendar.data.user.model

import com.google.gson.annotations.SerializedName

class TokenResponse {
    @SerializedName("token")
    var token: String? = null
}