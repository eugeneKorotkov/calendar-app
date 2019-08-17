package com.vio.calendar.data.article

import androidx.lifecycle.LiveData
import com.vio.calendar.data.article.model.*

interface ArticleRepository {

    fun getComments(article: Article): LiveData<List<Comment>?>

    fun saveArticle(article: Article)

    fun deleteArticle(article: Article)

    fun getArticles(code: String): LiveData<List<Article>?>

    fun getLikesCount(article: Article): LiveData<LikesResponseCount?>

    fun getLikes(article: Article): LiveData<List<LikesResponseItem>?>

    fun like(article: Article, token: String)

    fun unlike(article: Article, token: String)

    fun sendComment(articleId: String, api_key: String, comment: CommentSend)
}