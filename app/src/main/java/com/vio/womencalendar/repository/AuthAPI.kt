package com.vio.womencalendar.repository

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import com.vio.womencalendar.model.token.Token
import com.vio.womencalendar.model.user.User

interface AuthAPI {
    @Headers(
        "Accept: application/json",
        "Content-type:application/json")
    @POST("users/sing-up")
    fun getAccessToken(@Body user: User): Call<Token>

    @POST("users/log-in")
    fun login(@Body user: User): Call<Token>
}