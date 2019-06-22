package ru.korotkov.womencalendar.model.user

import com.google.gson.annotations.SerializedName

data class UserInfo (
    @SerializedName("lengthCycle")
    val lengthCycle: Int,
    @SerializedName("lengthMenstrual")
    val lengthMenstrual: Int
)