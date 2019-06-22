package ru.korotkov.womencalendar.app

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.korotkov.womencalendar.repository.ServerAPI

object Injection {

    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/eugene-korotkov/emaxx/master/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideServerAPI(): ServerAPI {
        return provideRetrofit().create(ServerAPI::class.java)
    }
}