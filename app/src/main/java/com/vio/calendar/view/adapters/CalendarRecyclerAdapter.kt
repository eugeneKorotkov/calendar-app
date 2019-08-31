package com.vio.calendar.view.adapters

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


class CalendarRecyclerAdapter(
    private val days: ArrayList<CalendarCell>,
    private val firstDay: Int,
    private val listener: (CalendarCell) -> Unit
) :
    RecyclerView.Adapter<CalendarRecyclerAdapter.DayViewHolder>() {

    private val calToday = GregorianCalendar()
    private val dayToday = calToday.get(GregorianCalendar.DATE)
    private val monthToday = calToday.get(GregorianCalendar.MONTH) + 1
    private val yearToday = calToday.get(GregorianCalendar.YEAR)

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

                if (calendarCell.day == dayToday &&
                    calendarCell.month == monthToday &&
                    calendarCell.year == yearToday
                ) calendarCell.iscurrent = true

                when (calendarCell.type) {

                    EMPTY -> if (calendarCell.iscurrent) itemView.dayName.underline()


                    OVULATION_PREDICTED -> {
                        itemView.dayName.setTextColor(Color.WHITE)
                        if (calendarCell.iscurrent) {
                            itemView.dayName.underline()
                        } else {
                            itemView.dayLinear.setBackgroundResource(R.drawable.ovulation_predicted)
                        }
                    }
                    OVULATION_FUTURE -> {
                        itemView.dayName.setTextColor(Color.WHITE)
                        if (calendarCell.iscurrent) {
                            itemView.dayName.underline()
                        } else {
                            itemView.dayLinear.setBackgroundResource(R.drawable.ovulation_predicted)
                            //itemView.dayLinear.setBackgroundResource(R.drawable.ovulation_predicted)
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




                itemView.setOnClickListener {
                    Log.d("CalendarRecyclerAdapter", "calendarCell.type = " + calendarCell.type)
                    listener(calendarCell)
                }
            } else {
                itemView.dayName.text = ""
            }

        }
    }


}