package ru.korotkov.womencalendar.ui.steps

import android.content.Context
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.steps_day_item_row.view.*
import ru.korotkov.womencalendar.R
import ru.korotkov.womencalendar.inflate
import ru.korotkov.womencalendar.model.date.Date
import ru.korotkov.womencalendar.model.date.DateRange
import ru.korotkov.womencalendar.model.date.dayOfWeek
import ru.korotkov.womencalendar.model.date.daysInMonth


class StepsMonthAdapter (val context: Context, private val month: DateRange): RecyclerView.Adapter<StepsMonthAdapter.ViewHolder>() {

    private var firstDay = month.elementAt(0)
    private var dayOfWeek = dayOfWeek(firstDay.getYear(), firstDay.getMonth(), firstDay.getDay())

    override fun getItemCount(): Int {
        return dayOfWeek + daysInMonth(firstDay.getMonth(), firstDay.getYear())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> holder.hide()
            1 -> holder.bind(month.elementAt(position))
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.steps_day_item_row))
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private lateinit var day: Date

        fun bind(day: Date) {
            this.day = day
            itemView.stepsDayTitle.text = day.toString()
        }

        fun hide() {
            itemView.stepsDayTitle.text = ""
            itemView.stepsDayButton.visibility = GONE
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position < dayOfWeek) return 0
        return 1
    }
}


