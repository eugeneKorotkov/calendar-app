package com.vio.womencalendar.ui.articles

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.list_item_article.view.*
import com.vio.womencalendar.R
import com.vio.womencalendar.inflate
import com.bumptech.glide.request.target.Target
import com.vio.womencalendar.model.arcticle.Article

class ArticleAdapter(private val articles: MutableList<Article>): RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_article))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount() = articles.size

    fun updateArticles(articles: List<Article>) {
        this.articles.clear()
        this.articles.addAll(articles)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private lateinit var article: Article

        fun bind(article: Article) {
            this.article = article
            itemView.articleShortDescription.text = article.shortDescription
            itemView.articleTitle.text = article.title

            Log.d("ArticleAdapter", "hey dude: " + article.image)

            Glide
                .with(itemView.context)
                .load(article.image)
                .apply(RequestOptions()
                    .override(Target.SIZE_ORIGINAL))
                .into(itemView.articleImage)

        }
    }

}