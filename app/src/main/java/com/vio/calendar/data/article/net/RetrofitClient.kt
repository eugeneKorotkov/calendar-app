package com.vio.calendar.data.article.net

import android.util.Log
import com.vio.calendar.data.article.model.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    private val articlesApi: ArticlesApi

    companion object {
        private const val API_BASE_URL = "http://134.209.23.52/api/v1/"
    }

    init {
        val builder = OkHttpClient.Builder()
        val okHttpClient = builder.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        articlesApi = retrofit.create(ArticlesApi::class.java)
    }

    fun getArticles(code: String): Call<List<Article>?> {
        return articlesApi.getArticles(code)
    }

    fun getComments(article: Article): Call<List<Comment>?> {
        return articlesApi.getComment(article.id!!)
    }

    fun getLikesCount(article: Article): Call<LikesResponseCount> {
        Log.d("getLikesCount", "retrofit - article ${article.id}")
        return articlesApi.getLikesCount(article.id!!)
    }

    fun sendComment(articleId: String, api_key: String, comment: CommentSend): Call<Any> {
        return articlesApi.sendComment(articleId, api_key, comment)
    }

    fun like(article: Article,  token: String): Call<Any> {
        return articlesApi.like(article.id!!, token)
    }

    fun unlike(article: Article,  token: String): Call<Any> {
        return articlesApi.unlike(article.id!!, token)
    }

    fun getLikes(article: Article): Call<List<LikesResponseItem>> {
        return articlesApi.getLikes(article.id!!)
    }
}