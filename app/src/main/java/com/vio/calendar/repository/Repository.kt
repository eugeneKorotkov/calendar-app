package com.vio.calendar.repository

import androidx.lifecycle.LiveData
import com.vio.calendar.model.arcticle.Article

interface Repository {
  fun getArticles(): LiveData<List<Article>>
  fun getArticleLikeCount(article: Article): Int
}

