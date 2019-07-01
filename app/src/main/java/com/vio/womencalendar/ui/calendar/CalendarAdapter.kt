package com.vio.womencalendar.ui.calendar
/*
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_month.view.*
import ru.korotkov.womencalendar.R
import ru.korotkov.womencalendar.inflate
import ru.korotkov.womencalendar.model.Month

class CalendarAdapter (private val months: List<Month>, val context: Context): RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    override fun getItemCount() = months.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(months[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_month))
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private lateinit var month: Month

        fun bind(month: Month) {
            this.month = month
            val adapter = MonthAdapter(context, month.days)
            itemView.monthGrid.adapter = adapter
        }
    }

}*/