package com.vio.calendar.data.user.model

import com.google.gson.annotations.SerializedName

data class UserFetchResponse (
 @SerializedName("record")
 var record: UserFetchRecord
)