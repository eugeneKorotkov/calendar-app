package com.vio.womencalendar.ui.steps

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.steps_month_item_row.view.*
import com.vio.womencalendar.R
import com.vio.womencalendar.inflate
import com.vio.womencalendar.model.date.Date
import com.vio.womencalendar.model.date.DateRange
import com.vio.womencalendar.model.date.daysInMonth
import com.vio.womencalendar.model.date.monthStringId

class StepsCalendarAdapter (val context: Context, private val dateRange: DateRange, val listener: (Date) -> Unit): RecyclerView.Adapter<StepsCalendarAdapter.ViewHolder>() {

    override fun getItemCount() = dateRange.numberOfMonth()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemDateStart = dateRange.start.incMonth(position)
        val itemDateEnd = Date(itemDateStart.getYear(), itemDateStart.getMonth(), daysInMonth(itemDateStart.getMonth(), itemDateStart.getYear()))

        holder.bind(itemDateStart..itemDateEnd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.steps_month_item_row))
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private lateinit var month: DateRange

        fun bind(m: DateRange) {
            this.month = m
            itemView.stepsMonthName.text = context.getText(monthStringId(month.start.getMonth()))
            itemView.stepsMonthRecycler.layoutManager = GridLayoutManager(context, 7)
            itemView.stepsMonthRecycler.adapter = StepsMonthAdapter(context, month, listener)
        }
    }
}