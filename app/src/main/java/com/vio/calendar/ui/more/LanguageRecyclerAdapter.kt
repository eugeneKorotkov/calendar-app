package com.vio.calendar.ui.more

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vio.calendar.R
import com.vio.calendar.inflate
import kotlinx.android.synthetic.main.item_language.view.*

class LanguageRecyclerAdapter(private val languages: ArrayList<LanguageItem>, private val listener: (LanguageItem) -> Unit): RecyclerView.Adapter<LanguageRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(parent.inflate(R.layout.item_language))
    override fun getItemCount() = languages.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = languages[position]
        holder.bind(item, listener)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(languageItem: LanguageItem, listener: (LanguageItem) -> Unit) {
            itemView.textLanguage.text = languageItem.name
            itemView.imageLanguage.setImageResource(languageItem.image)
            itemView.setOnClickListener{ listener(languageItem)}
        }
    }
}