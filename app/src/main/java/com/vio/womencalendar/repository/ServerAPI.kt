package com.vio.womencalendar.repository

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.http.*
import com.vio.womencalendar.model.arcticle.Article

interface ServerAPI {

    @GET("posts")
    fun getArticles(): Call<List<Article>>

    /*@POST("gists")
    fun postGist(@Body body: GistRequest): Call<Gist>*/

}
