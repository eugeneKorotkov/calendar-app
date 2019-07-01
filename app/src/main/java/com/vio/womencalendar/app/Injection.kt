package com.vio.womencalendar.app

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.vio.womencalendar.repository.AuthAPI
import com.vio.womencalendar.repository.RemoteRepository
import com.vio.womencalendar.repository.Repository
import com.vio.womencalendar.repository.ServerAPI

object Injection {

    fun provideRepository(): Repository = RemoteRepository

    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://134.209.23.52/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideServerAPI(): ServerAPI {
        return provideRetrofit().create(ServerAPI::class.java)
    }

    fun provideAuthAPI(): AuthAPI {
        return provideRetrofit().create(AuthAPI::class.java)
    }
}