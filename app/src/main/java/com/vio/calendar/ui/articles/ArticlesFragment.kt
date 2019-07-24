package com.vio.calendar.ui.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.vio.calendar.R
import com.vio.calendar.model.arcticle.Article
import com.vio.calendar.viewmodel.ArticleViewModel
import kotlinx.android.synthetic.main.fragment_articles.*

class ArticlesFragment: Fragment()  {

    private lateinit var articleViewModel: ArticleViewModel
    private val adapter = ArticleAdapter(mutableListOf())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_articles, container, false)
        articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articlesRecyclerView.layoutManager = LinearLayoutManager(context)
        articlesRecyclerView.adapter = adapter

        getArticles()

        articlesSwipeRefresh.setOnRefreshListener {
            getArticles()
        }


    }

    private fun getArticles() {
        articleViewModel.getArticles().observe(this, Observer<List<Article>> {
            adapter.updateArticles(it)
            articlesSwipeRefresh.isRefreshing = false
        })
    }
}
