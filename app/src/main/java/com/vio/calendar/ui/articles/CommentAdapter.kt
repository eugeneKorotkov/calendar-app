package com.vio.calendar.ui.articles

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vio.calendar.R
import com.vio.calendar.data.article.model.Comment
import com.vio.calendar.inflate
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentAdapter(private val comments: MutableList<Comment>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_comment))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount() = comments.size

    fun setArticles(comments: List<Comment>) {
        this.comments.clear()
        this.comments.addAll(comments)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private lateinit var comment: Comment

        fun bind(comment: Comment) {
            this.comment = comment
            itemView.name.text = "simpleName"
            itemView.comment.text = comment.content
        }
    }

}
