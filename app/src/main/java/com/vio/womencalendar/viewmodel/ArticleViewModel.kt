package com.vio.womencalendar.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.vio.womencalendar.app.Injection

class ArticleViewModel(application: Application): AndroidViewModel(application) {
    private val repository = Injection.provideRepository()
    private val allArticles = repository.getArticles()

    fun getArticles() = allArticles

}