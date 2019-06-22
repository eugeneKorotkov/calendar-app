package ru.korotkov.womencalendar.ui.articles

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_article.view.*
import ru.korotkov.womencalendar.R
import ru.korotkov.womencalendar.inflate
import ru.korotkov.womencalendar.model.arcticle.Article

class ArticleAdapter(private val articles: List<Article>): RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_article))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount() = articles.size


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private lateinit var article: Article

        fun bind(article: Article) {
            this.article = article
            itemView.articleShortDescription.text = article.shortDescription
            itemView.articleTitle.text = article.title

            Glide.with(itemView.context).load(article.image).into(itemView.articleImage)

        }
    }

}