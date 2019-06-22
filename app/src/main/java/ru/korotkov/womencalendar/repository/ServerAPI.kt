package ru.korotkov.womencalendar.repository

import retrofit2.Call
import retrofit2.http.*
import ru.korotkov.womencalendar.model.arcticle.Article

interface ServerAPI {

    @GET("qweasdf.json")
    fun getArticles(): Call<List<Article>>


}
