package com.vio.womencalendar.ui.articles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_articles.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.vio.womencalendar.R
import com.vio.womencalendar.app.Injection
import com.vio.womencalendar.model.arcticle.Article
import com.vio.womencalendar.viewmodel.ArticleViewModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class ArticlesFragment: Fragment()  {

    private lateinit var articleViewModel: ArticleViewModel

    private val api = Injection.provideServerAPI()
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

        art
    }

    private fun getArticles() {
        articleViewModel.getArticles().observe(this, Observer<List<Article>> {
            adapter.updateArticles(it)
            articlesSwipeRefresh.isRefreshing = false
        })
    }
}
