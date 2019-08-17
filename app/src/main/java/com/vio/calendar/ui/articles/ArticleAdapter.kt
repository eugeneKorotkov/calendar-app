package com.vio.calendar.ui.articles

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vio.calendar.R
import com.vio.calendar.data.article.ArticleRepositoryImpl
import com.vio.calendar.data.article.model.Article
import com.vio.calendar.data.article.model.CommentSend
import com.vio.calendar.inflate
import kotlinx.android.synthetic.main.item_article.view.*

class ArticleAdapter(private val articles: MutableList<Article>, private val lifecycleOwner: LifecycleOwner, private val context: Context, private val token: String) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    val repository = ArticleRepositoryImpl()

    val adapter = CommentAdapter(mutableListOf())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_article))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount() = articles.size

    fun setArticles(articles: List<Article>) {
        this.articles.clear()
        this.articles.addAll(articles)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private lateinit var article: Article

        fun bind(article: Article) {
            this.article = article
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                itemView.articleContent.text = Html.fromHtml(article.content, Html.FROM_HTML_MODE_LEGACY)
            } else {
                itemView.articleContent.text = Html.fromHtml(article.content)
            }
            itemView.articleTitle.text = article.title

            itemView.articleCard.setOnClickListener {
                if (itemView.contentLayout.isExpanded) {
                    itemView.contentLayout.collapse()
                } else {
                    itemView.contentLayout.expand()
                }
            }

            itemView.articleComment.setOnClickListener {
                if (itemView.commentLayout.isExpanded) {
                    itemView.commentLayout.collapse()
                } else {
                    itemView.commentLayout.expand()
                }
            }

            itemView.commentRecycler.layoutManager = LinearLayoutManager(context)
            itemView.commentRecycler.adapter = adapter

            repository.getLikes(article)

            itemView.articleLike.setOnClickListener {
                Log.d("ArticleAdapter", "token $token")
                repository.like(article, token)
            }

            itemView.sendCommentButton.setOnClickListener {
                repository.sendComment(article.id!!, token, CommentSend(itemView.commentInputField.text.toString()))
            }

            repository.getLikesCount(article).observe(lifecycleOwner, Observer {
                likeResponseCount ->
                Log.d("getLikesCount", "article.id ${article.id}")
                itemView.articleLikeCount.text = likeResponseCount?.likesCount.toString()
            })



            repository.getComments(article).observe(lifecycleOwner, Observer {
                    articles ->
                if (articles == null) {
                } else {
                    adapter.setArticles(articles)
                }
            })

            Glide
                .with(itemView.context)
                .load(article.image)
                .apply(
                    RequestOptions().centerCrop())
                .into(itemView.articleImage)

        }
    }

}
