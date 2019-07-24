package com.vio.calendar.ui.articles

import android.os.Build
import android.text.Html
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vio.calendar.R
import com.vio.calendar.inflate
import com.vio.calendar.model.arcticle.Article
import kotlinx.android.synthetic.main.item_article.view.*

class ArticleAdapter(private val articles: MutableList<Article>): RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_article))
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                itemView.articleContent.text = Html.fromHtml(article.content, Html.FROM_HTML_MODE_LEGACY)
            } else {
                itemView.articleContent.text = Html.fromHtml(article.content)
            }
            //itemView.articleTitle.text = article.title
            //itemView.articleLikeCount.text = "4"

            /*Glide
                .with(itemView.context)
                .load(article.image)
                .apply(RequestOptions()
                    .override(Target.SIZE_ORIGINAL))
                .into(itemView.articleImage)*/
            itemView.expand_button.setOnClickListener {
                if (itemView.expandableLayout.isExpanded) {
                    itemView.expandableLayout.collapse();
                } else {
                    itemView.expandableLayout.expand();
                }
            }

        }
    }

}