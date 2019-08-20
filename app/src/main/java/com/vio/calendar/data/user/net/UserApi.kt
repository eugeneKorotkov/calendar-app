package com.vio.calendar.data.user.net

import com.vio.calendar.data.user.model.TokenResponse
import com.vio.calendar.data.user.model.User
import com.vio.calendar.data.user.model.UserData
import com.vio.calendar.data.user.model.UserFetchResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {

    @POST("sign-up")
    fun getToken(@Body user: User): Call<TokenResponse>

    @GET("current/fetch")
    fun getUserId(@Query("token") token: String): Call<UserFetchResponse>

    @POST("current/update")
    fun updateUser(@Query("token") token: String, @Body userData: UserData): Call<Any>

}