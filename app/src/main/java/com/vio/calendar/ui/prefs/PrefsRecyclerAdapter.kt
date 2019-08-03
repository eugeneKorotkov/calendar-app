package com.vio.calendar.ui.prefs

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vio.calendar.R
import com.vio.calendar.inflate
import com.vio.calendar.model.prefs.PreferenceItem
import kotlinx.android.synthetic.main.item_prefs.view.*

class PrefsRecyclerAdapter(private val prefsList: ArrayList<PreferenceItem>): RecyclerView.Adapter<PrefsRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(parent.inflate(R.layout.item_prefs))
    }

    override fun getItemCount() = prefsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position != 3) holder.bind(prefsList[position])
        else holder.bindEmpty()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(preference: PreferenceItem) {
            itemView.imagePreferences.setImageResource(preference.image)
            itemView.titlePreferences.text = preference.key
            itemView.summaryPrefs.text = preference.title
            itemView.valuePrefs.text = preference.value.toString()
        }

        fun bindEmpty() {
            itemView.constraintPreferences.visibility = View.INVISIBLE
        }
    }
}