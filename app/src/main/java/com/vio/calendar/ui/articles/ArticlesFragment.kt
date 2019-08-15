package com.vio.calendar.ui.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vio.calendar.R
import com.vio.calendar.ui.main.MainActivity
import com.vio.calendar.viewmodel.article.ArticleViewModel
import kotlinx.android.synthetic.main.fragment_articles.*

class ArticlesFragment: Fragment()  {

    private lateinit var adapter: ArticleAdapter
    private lateinit var articleViewModel: ArticleViewModel

    var code: String = "es"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
        adapter = ArticleAdapter(mutableListOf(), this, (activity as MainActivity).applicationContext)
        articlesRecyclerView.adapter = adapter
        code = (activity as MainActivity).code
        getArticles()
    }

    private fun showLoading() {
        articlesRecyclerView.isEnabled = false
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        articlesRecyclerView.isEnabled = true
        progressBar.visibility = View.GONE
    }

    private fun getArticles() {
        showLoading()
        articleViewModel.getArticles(code).observe(this, Observer { articles ->
            hideLoading()
            if (articles == null) {
            } else {
                adapter.setArticles(articles)
            }
        })
    }

}
