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
import kotlinx.android.synthetic.main.day_item_row.view.*




class CalendarRecyclerAdapter(val days: ArrayList<CalendarCell>, private val firstDay: Int, private val listener: (CalendarCell) -> Unit, private val listenerLongClick: (CalendarCell) -> Boolean) :
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
        holder.bindItem(day, listener, listenerLongClick, position)
    }


    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(calendarCell: CalendarCell, listener: (CalendarCell) -> Unit, listenerLongClick: (CalendarCell) -> Boolean, position: Int) {

            if (position + 1 >= firstDay) {

                itemView.dayName.text = calendarCell.day.toString()

                when (calendarCell.type) {

                    OVULATION_PREDICTED -> {
                        itemView.dayName.setTextColor(Color.WHITE)
                        itemView.dayLinear.setBackgroundResource(R.drawable.ovulation_center)
                    }
                    OVULATION_FUTURE -> {
                        itemView.dayName.setTextColor(Color.WHITE)
                        itemView.dayLinear.setBackgroundResource(R.drawable.ovulation_center_future)
                    }

                    PERIOD_START -> {
                        itemView.dayName.setTextColor(Color.parseColor("#FFFFFF"))
                        itemView.dayLinear.setBackgroundResource(R.drawable.period_start)

                        if (position + 1 < days.size) {
                            if (days[position + 1].type != PERIOD_CONFIRMED && days[position + 1].type != PERIOD_PREDICTED) {
                                itemView.dayLinear.setBackgroundResource(R.drawable.period_one)
                            }
                        }
                        if (calendarCell.iscurrent) {
                            itemView.dayLinear.setBackgroundResource(R.drawable.period_today)
                            itemView.dayName.setTextColor(Color.RED)
                        }

                    }
                    PERIOD_CONFIRMED -> {
                        itemView.dayName.setTextColor(Color.parseColor("#FFFFFF"))

                        if (position > 0 && position + 1 < days.size) {
                            if (days[position - 1].iscurrent)
                                itemView.dayLinear.setBackgroundResource(R.drawable.period_start)
                            else if (days[position + 1].type != PERIOD_CONFIRMED || days[position + 1].iscurrent) {
                                itemView.dayLinear.setBackgroundResource(R.drawable.period_end)
                            } else {
                                itemView.dayLinear.setBackgroundResource(R.drawable.period_center)
                            }
                        } else {
                            itemView.dayLinear.setBackgroundResource(R.drawable.period_end)
                        }

                        if (calendarCell.iscurrent) {
                            itemView.dayLinear.setBackgroundResource(R.drawable.period_today)
                            itemView.dayName.setTextColor(Color.RED)
                        }
                    }
                    PERIOD_PREDICTED -> {
                        if ((position > 0 ) && (position + 1 < days.size)) {

                            itemView.dayName.setTextColor(Color.parseColor("#FFFFFF"))

                            val left = days[position - 1]
                            val right = days[position + 1]

                            if (left.type != PERIOD_PREDICTED &&
                                left.type != PERIOD_CONFIRMED &&
                                left.type != PERIOD_START) {
                                if (right.type != PERIOD_PREDICTED &&
                                    right.type != PERIOD_CONFIRMED &&
                                    right.type != PERIOD_START) {
                                    itemView.dayLinear.setBackgroundResource(R.drawable.period_one_future)
                                } else {
                                    itemView.dayLinear.setBackgroundResource(R.drawable.period_start_future)
                                }
                            }  else {
                                if (right.type != PERIOD_PREDICTED &&
                                    right.type != PERIOD_CONFIRMED &&
                                    right.type != PERIOD_START) {
                                    itemView.dayLinear.setBackgroundResource(R.drawable.period_end_future)
                                } else {
                                    itemView.dayLinear.setBackgroundResource(R.drawable.period_center_future)
                                }
                            }
                        }

                        if (calendarCell.iscurrent) {
                            itemView.dayLinear.setBackgroundResource(R.drawable.period_today)
                            itemView.dayName.setTextColor(Color.RED)
                        }

                    }
                    FERTILITY_PREDICTED -> {
                        itemView.dayName.setTextColor(Color.parseColor("#5094A9"))
                        if (calendarCell.iscurrent) itemView.dayLinear.setBackgroundResource(R.drawable.fertility_today)
                    }
                    FERTILITY_FUTURE -> {
                        itemView.dayName.setTextColor(Color.parseColor("#7F5094A9"))
                        if (calendarCell.iscurrent) itemView.dayLinear.setBackgroundResource(R.drawable.fertility_today)
                    }

                    INFERTILE_PREDICTED, INFERTILE_FUTURE -> {
                        if (calendarCell.iscurrent) {
                            itemView.dayLinear.setBackgroundResource(R.drawable.infertility_today)
                            itemView.dayName.setTextColor(Color.RED)
                        }
                    }
                }


                itemView.setOnLongClickListener{
                    Log.d("CalendarRecyclerAdapter", "longClick!")
                    listenerLongClick(calendarCell)
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
