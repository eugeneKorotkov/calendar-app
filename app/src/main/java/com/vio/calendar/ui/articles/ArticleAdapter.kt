package com.vio.calendar.ui.articles

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.MotionEvent
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
import com.vio.calendar.data.article.model.Comment
import com.vio.calendar.data.article.model.CommentSend
import com.vio.calendar.data.article.model.LikesResponseItem
import com.vio.calendar.data.user.model.UserData
import com.vio.calendar.inflate
import kotlinx.android.synthetic.main.item_article.view.*
import java.util.*


class ArticleAdapter(private val articles: MutableList<Article>, private val lifecycleOwner: LifecycleOwner, private val context: Context, private val prefs: SharedPreferences) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    val repository = ArticleRepositoryImpl()



    val adapter = CommentAdapter(mutableListOf())
    var likesCount: Int = 0
    var commentsCount: Int = 0
    var isLiked: Boolean = false

    val token = prefs.getString("token", "s")!!
    val name = prefs.getString("user_name", "")!!
    val id = prefs.getString("userid", "")!!
    val color = prefs.getString("color", "")!!

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

            val linearLayoutManager = LinearLayoutManager(context)

            itemView.commentRecycler.layoutManager = linearLayoutManager
            itemView.commentRecycler.adapter = adapter


            val mScrollTouchListener = object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                     val action = e.action
                    when (action) {
                        MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(true)
                    }
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

                }
            }

            itemView.commentRecycler.addOnItemTouchListener(mScrollTouchListener)

            repository.getLikes(article).observe(lifecycleOwner, Observer {
                likesList ->
                if (likesList!!.contains(LikesResponseItem(id))) {
                    isLiked = true
                    itemView.articleLike.setImageResource(R.drawable.ic_like_true)
                } else {
                    isLiked = false
                    itemView.articleLike.setImageResource(R.drawable.ic_like_false)
                }

            })

            itemView.articleLike.setOnClickListener {

                if (!isLiked) {
                    Log.d("ArticleAdapter", "token $token")
                    repository.like(article, token)
                    isLiked = true
                    itemView.articleLike.setImageResource(R.drawable.ic_like_true)
                    likesCount++
                    itemView.articleLikeCount.text = likesCount.toString()
                } else {
                    Log.d("ArticleAdapter", "token $token")
                    repository.unlike(article, token)
                    isLiked = false
                    Log.d("userId", "sss: $id")
                    itemView.articleLike.setImageResource(R.drawable.ic_like_false)
                    likesCount--
                    itemView.articleLikeCount.text = likesCount.toString()
                }


            }

            itemView.sendCommentButton.setOnClickListener {
                val comment = CommentSend(itemView.commentInputField.text.toString())
                repository.sendComment(article.id!!, token, comment)

                commentsCount++

                itemView.articleCommentCount.text = commentsCount.toString()

                adapter.addComment(Comment("", UserData(name), itemView.commentInputField.text.toString(), Date()))
            }

            repository.getLikesCount(article).observe(lifecycleOwner, Observer {
                likeResponseCount ->
                Log.d("getLikesCount", "article.id ${article.id}")
                likesCount = likeResponseCount?.likesCount!!
                itemView.articleLikeCount.text = likesCount.toString()
            })


            repository.getComments(article).observe(lifecycleOwner, Observer {
                    comments ->
                if (comments == null) {
                } else {

                    commentsCount = comments.size

                    itemView.articleCommentCount.text = comments.size.toString()

                    comments.sortedWith(compareBy {it.createdAt})
                    Log.d("articleAdapter", "size: ${comments.size}")
                    adapter.setComments(comments.asReversed())
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
