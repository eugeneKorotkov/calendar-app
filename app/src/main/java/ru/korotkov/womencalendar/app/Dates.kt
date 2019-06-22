package ru.korotkov.womencalendar.app

import ru.korotkov.womencalendar.Constants.LENGTH_FULL_CYCLE
import ru.korotkov.womencalendar.Constants.LENGTH_MENSTRUAL_CYCLE
import ru.korotkov.womencalendar.Constants.LUTEAL_LENGTH
import ru.korotkov.womencalendar.Constants.OVULATION_LENGTH
import ru.korotkov.womencalendar.Constants.START_DAY
import ru.korotkov.womencalendar.Constants.START_MONTH
import ru.korotkov.womencalendar.Constants.START_YEAR
import ru.korotkov.womencalendar.app.CalendarApplication.Companion.prefs
import ru.korotkov.womencalendar.model.date.Date
import ru.korotkov.womencalendar.model.date.DateRange
import ru.korotkov.womencalendar.model.date.DateType
import ru.korotkov.womencalendar.model.date.setMaximumDayInMonth
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