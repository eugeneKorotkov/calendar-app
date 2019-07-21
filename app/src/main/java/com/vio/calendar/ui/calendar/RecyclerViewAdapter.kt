package com.vio.calendar.ui.calendar

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vio.calendar.model.date.Month

typealias ClickListener = (Month) -> Unit

class RecyclerViewAdapter: PagedListAdapter<Month, RecyclerViewAdapter.ViewHolder>(diffCallback) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewAdapter.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {
        val month = getItem(position)

        //

        if (month == null)
        {
            return;
        }
    }

    companion object {
        /**
         * This diff callback informs the PagedListAdapter how to compute list differences when new
         * PagedLists arrive.
         */
        private val diffCallback = object : DiffUtil.ItemCallback<Month>() {
            override fun areItemsTheSame(oldItem: Month, newItem: Month): Boolean =
                oldItem.compareTo(newItem) == 0

            override fun areContentsTheSame(oldItem: Month, newItem: Month): Boolean =
                oldItem.compareTo(newItem) == 0
        }
    }

}