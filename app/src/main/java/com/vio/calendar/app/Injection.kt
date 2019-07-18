package com.vio.calendar.app

import com.vio.calendar.repository.AuthAPI
import com.vio.calendar.repository.RemoteRepository
import com.vio.calendar.repository.Repository
import com.vio.calendar.repository.ServerAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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