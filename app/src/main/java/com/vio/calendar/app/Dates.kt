package com.vio.calendar.app

import com.vio.calendar.model.date.Date
import com.vio.calendar.model.date.DateRange
import com.vio.calendar.model.date.setMaximumDayInMonth
import java.util.Calendar

object Dates {

    private val todayDate: Date
    private val endDate: Date

    init {
        val calendar = Calendar.getInstance()
        todayDate = Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        endDate = todayDate.incMonth(3).setMaximumDayInMonth()
    }


    fun provideClearDates(i: Int): DateRange {
        val startDate = todayDate.decMonths(i)
        return startDate..todayDate.incMonth(i)
    }

}