package com.vio.womencalendar.app

import com.vio.womencalendar.Constants.LENGTH_FULL_CYCLE
import com.vio.womencalendar.Constants.LENGTH_MENSTRUAL_CYCLE
import com.vio.womencalendar.Constants.LUTEAL_LENGTH
import com.vio.womencalendar.Constants.OVULATION_LENGTH
import com.vio.womencalendar.Constants.START_DAY
import com.vio.womencalendar.Constants.START_MONTH
import com.vio.womencalendar.Constants.START_YEAR
import com.vio.womencalendar.app.CalendarApplication.Companion.prefs
import com.vio.womencalendar.model.date.Date
import com.vio.womencalendar.model.date.DateRange
import com.vio.womencalendar.model.date.DateType
import com.vio.womencalendar.model.date.setMaximumDayInMonth
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

    fun provideCalendarDates(): DateRange {

        var cycleStartDate = Date(
            prefs.getInt(START_YEAR, todayDate.getYear()),
            prefs.getInt(START_MONTH, todayDate.getMonth()),
            prefs.getInt(START_DAY, todayDate.getDay()))

        val lengthFullCycle = prefs.getInt(LENGTH_FULL_CYCLE, 0)
        val lengthMenstrualCycle = prefs.getInt(LENGTH_MENSTRUAL_CYCLE, 0)

        for (i in 1..lengthFullCycle) cycleStartDate--

        var count = 0
        for (day in cycleStartDate..endDate) {
            count++
            if (count % lengthFullCycle < lengthMenstrualCycle) day.type = DateType.MENSTRUAL
            else if (lengthFullCycle - (count % lengthFullCycle) <= LUTEAL_LENGTH + 1) day.type = DateType.LUTEAL
            else if ((count % lengthFullCycle <= lengthFullCycle - LUTEAL_LENGTH) && (count % lengthFullCycle > lengthFullCycle - LUTEAL_LENGTH - OVULATION_LENGTH)) day.type = DateType.OVULATION

        }
        return cycleStartDate.incMonth(0)..endDate
    }

}