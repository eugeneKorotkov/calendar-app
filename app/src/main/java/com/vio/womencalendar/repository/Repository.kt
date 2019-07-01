package com.vio.womencalendar.repository

import androidx.lifecycle.LiveData
import com.vio.womencalendar.model.arcticle.Article

interface Repository {
  fun getArticles(): LiveData<List<Article>>
}

