package com.vio.calendar.ui.calendar

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vio.calendar.R
import com.vio.calendar.db.PeriodicalDatabase.DayEntry.*
import com.vio.calendar.inflate
import com.vio.calendar.model.date.CalendarCell
import com.vio.calendar.underline
import kotlinx.android.synthetic.main.day_item_row.view.*
import java.util.*


class CalendarRecyclerAdapter(val days: ArrayList<CalendarCell>, private val firstDay: Int, private val listener: (CalendarCell) -> Unit) :
    RecyclerView.Adapter<CalendarRecyclerAdapter.DayViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        var inflatedView = parent.inflate(R.layout.day_item_row)
        return DayViewHolder(inflatedView)
    }


    override fun getItemCount(): Int {
        return days.size
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position]
        holder.bindItem(day, listener, position)
    }


    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(calendarCell: CalendarCell, listener: (CalendarCell) -> Unit, position: Int) {

            if (position + 1 >= firstDay) {

                itemView.dayName.text = calendarCell.day.toString()

                when (calendarCell.type) {




                    /*OVULATION_PREDICTED -> {
                        itemView.dayName.setTextColor(Color.WHITE)
                        if (calendarCell.iscurrent) {
                            itemView.dayName.underline()
                        } else {
                            itemView.dayLinear.setBackgroundResource(R.drawable.ovulation_predicted)
                        }
                    }*/
                    OVULATION_FUTURE, OVULATION_PREDICTED -> {
                        itemView.dayName.setTextColor(Color.WHITE)
                        if (calendarCell.iscurrent) {
                            itemView.dayName.underline()
                        } else {
                            itemView.dayLinear.setBackgroundResource(R.drawable.ovulation_future)
                        }
                    }

                    PERIOD_START, PERIOD_CONFIRMED -> {
                        itemView.dayName.setTextColor(Color.WHITE)

                        if (calendarCell.iscurrent) {
                            itemView.dayName.underline()
                        } else {
                            itemView.dayLinear.setBackgroundResource(R.drawable.period_predicted)
                        }

                    }
                    PERIOD_PREDICTED -> {
                        itemView.dayName.setTextColor(Color.WHITE)
                        /*
                        val i = GregorianCalendar(calendarCell.year, calendarCell.month - 1, calendarCell.day)
                        Log.d("CalendarRecyclerAdapter", "calendarCell: $i, today: $today")

                        if (i <= today) {
                            secondListener(i)
                        }
                        */
                        if (calendarCell.iscurrent) {
                            itemView.dayName.underline()
                        } else {
                            itemView.dayLinear.setBackgroundResource(R.drawable.period_predicted)
                        }

                    }
                    FERTILITY_PREDICTED -> {
                        itemView.dayName.setTextColor(Color.parseColor("#4A98A0"))
                        if (calendarCell.iscurrent) itemView.dayName.underline()
                    }
                    FERTILITY_FUTURE -> {
                        itemView.dayName.setTextColor(Color.parseColor("#6F4A98A0"))
                        if (calendarCell.iscurrent) itemView.dayName.underline()
                    }

                    INFERTILE_PREDICTED, INFERTILE_FUTURE -> {
                        if (calendarCell.iscurrent) itemView.dayName.underline()
                    }
                }


                itemView.setOnClickListener{
                    Log.d("CalendarRecyclerAdapter", "calendarCell.type = " + calendarCell.type)
                    listener(calendarCell)
                }
            } else {
                itemView.dayName.text = ""
            }

        }
    }


}
