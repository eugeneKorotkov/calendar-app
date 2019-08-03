package com.vio.calendar.ui.articles

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.vio.calendar.R
import com.vio.calendar.inflate
import com.vio.calendar.model.article.Article
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.item_article.view.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ArticleAdapter(private val articles: MutableList<Article>, private val context: Context): RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

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

            this.article = article // ...

// Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(context)

            val id = article.id
            val url = "http://134.209.23.52/api/v1/likes/post.id/$id/count"
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                Response.Listener<String> { response ->
                    var jsonString = JSONObject(response)
                    itemView.articleLikeCount.text = jsonString.getInt("count").toString()
                    Log.d("ArticleAdapter","Response is: $response")
                },
                Response.ErrorListener {})
            queue.add(stringRequest)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                itemView.articleContent.text = Html.fromHtml(article.content, Html.FROM_HTML_MODE_LEGACY)
            } else {
                itemView.articleContent.text = Html.fromHtml(article.content)
            }
            itemView.articleTitle.text = article.title
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                itemView.articleImage.clipToOutline = true
            }
            Glide
                .with(itemView.context)
                .load(article.image)
                .apply(RequestOptions().transforms(BlurTransformation(25), CenterCrop()))
                .into(itemView.articleImage)


            itemView.articleComment.setOnClickListener{

                if (itemView.commentLayout.isExpanded) {
                    itemView.commentLayout.collapse()
                } else {
                    itemView.commentLayout.expand()
                }

            }

            itemView.articleImage.setOnClickListener {
                if (itemView.contentLayout.isExpanded) {
                    if (itemView.commentLayout.isExpanded) itemView.commentLayout.collapse()
                    itemView.contentLayout.collapse();
                } else {
                    itemView.contentLayout.expand();
                }

            }

        }
    }


    fun sendGetRequest(id:String) {

        var mURL = URL("http://134.209.23.52/api/v1/likes/post.id/$id/count")

        with(mURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "GET"

            println("URL : $url")
            println("Response Code : $responseCode")

            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()

                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                println("Response : $response")

                Log.d("ArticleAdapter", "response :$response")
            }
        }
    }

}