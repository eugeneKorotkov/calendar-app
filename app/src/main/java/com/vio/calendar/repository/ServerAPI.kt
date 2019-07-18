package com.vio.calendar.repository

import retrofit2.Call
import retrofit2.http.*
import com.vio.calendar.model.arcticle.Article
import com.vio.calendar.model.arcticle.LikesCount

interface ServerAPI {

    @GET("posts")
    fun getArticles(): Call<List<Article>>

    @GET("likes/post.id/{id}/count")
    fun getArticleCount(@Path("id") id: String): Call<LikesCount>

    /*@POST("gists")
    fun postGist(@Body body: GistRequest): Call<Gist>*/

}
