package com.vio.calendar.data.user

import android.util.Log
import com.vio.calendar.data.user.model.TokenResponse
import com.vio.calendar.data.user.model.User
import com.vio.calendar.data.user.model.UserFetchResponse
import com.vio.calendar.data.user.net.UserRetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    private val retrofitClient = UserRetrofitClient()

    fun getToken(user: User): String? {
        var token: String? = null
        retrofitClient.getToken(user).enqueue(object : Callback<TokenResponse> {
            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Log.d("UserRepository", "Failure")
            }
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                token = response.body()?.token
                Log.d(" UserRepository", "Response: ${response.body()?.token}")
            }
        })
        return token
    }

    fun getUserId(token: String): String? {
        var id: String? = null
        retrofitClient.getUserId(token).enqueue(object: Callback<UserFetchResponse> {
            override fun onFailure(call: Call<UserFetchResponse>, t: Throwable) {
                Log.d("UserRepository", "getUserId: failure")
            }

            override fun onResponse(call: Call<UserFetchResponse>, response: Response<UserFetchResponse>) {
                id = response.body()?.record?.id
            }
        })

        return id
    }
}