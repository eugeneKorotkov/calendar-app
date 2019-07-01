package com.vio.womencalendar.ui.steps

import android.content.Context
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.steps_day_item_row.view.*
import com.vio.womencalendar.R
import com.vio.womencalendar.inflate
import com.vio.womencalendar.model.date.Date
import com.vio.womencalendar.model.date.DateRange
import com.vio.womencalendar.model.date.dayOfWeek
import com.vio.womencalendar.model.date.daysInMonth


class StepsMonthAdapter (val context: Context, private val month: DateRange, private val listener: (Date) -> Unit): RecyclerView.Adapter<StepsMonthAdapter.ViewHolder>() {

    private var firstDay = month.elementAt(0)
    private var dayOfWeek = dayOfWeek(firstDay.getYear(), firstDay.getMonth(), firstDay.getDay())


    private var clickedItem : View? = null

    override fun getItemCount(): Int {
        return dayOfWeek + daysInMonth(firstDay.getMonth(), firstDay.getYear())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> holder.hide()
            1 -> holder.bind(month.elementAt(position - dayOfWeek), listener)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.steps_day_item_row))
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


        fun bind(day: Date, listener: (Date) -> Unit) {
            itemView.stepsDayTitle.text = day.getDay().toString()
            itemView.stepsDayButton.setOnClickListener {
                Log.d("Korotkov", "buttonClickedSS")
                if (clickedItem == null) clickedItem = it
                else {
                    (clickedItem as RadioButton).isChecked = false
                    clickedItem = it
                }
                listener(day)}
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


