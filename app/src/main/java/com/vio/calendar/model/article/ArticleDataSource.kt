package com.vio.calendar.model.article

import android.app.Application
import android.database.Observable
import kotlin.concurrent.thread

open class ArticleDataSource(application : Application) {

    private val articleDao: ArticleDao
    open val allArticles: Observable<List<Article>>

    init {
        val db = ArticleDatabase.getInstance(application)
        articleDao = db.articleDao()
        allArticles = articleDao.all
    }

    fun insert(article: Article) {
        thread {
            articleDao.insert(article)
        }
    }

    fun delete(article: Article) {
        thread {
            articleDao.delete(article.id)
        }
    }

    fun update(article: Article) {
        thread {
            articleDao.update(article)
        }
    }
}