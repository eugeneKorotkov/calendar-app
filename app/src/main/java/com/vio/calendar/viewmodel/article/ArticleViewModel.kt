package com.vio.calendar.viewmodel.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.vio.calendar.data.article.ArticleRepository
import com.vio.calendar.data.article.ArticleRepositoryImpl
import com.vio.calendar.data.article.model.Article
import com.vio.calendar.data.article.model.Comment

class ArticleViewModel(private val repository: ArticleRepository = ArticleRepositoryImpl()): ViewModel() {

    private val allArticles = MediatorLiveData<List<Article>>()

    init {
        getAllArticles()
    }

    fun getSavedArticles() = allArticles

    private fun getAllArticles() {
    }

    fun getArticles(code: String): LiveData<List<Article>?> {
        return repository.getArticles(code)
    }

    fun deleteSavedArticles(article: Article) {
        repository.deleteArticle(article)
    }

    fun getComments(article: Article): LiveData<List<Comment>?> {
        return repository.getComments(article)
    }

}