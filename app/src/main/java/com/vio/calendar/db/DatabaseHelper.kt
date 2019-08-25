package com.vio.calendar.db

import android.app.Application
import android.app.backup.BackupManager
import com.vio.calendar.model.date.CalendarCell
import java.util.*

class DatabaseHelper(application: Application) {

    private var dbMain: PeriodicalDatabase = PeriodicalDatabase(application)
    private val bm = BackupManager(application)

    init {
        dbMain.restorePreferences()
        dbMain.loadCalculatedData()
    }

    fun addPeriod(date: GregorianCalendar) {
        dbMain.addPeriod(date)
        dbMain.loadCalculatedData()
        bm.dataChanged()
    }

    fun removePeriod(date: GregorianCalendar) {
        dbMain.removePeriod(date)
        dbMain.loadCalculatedData()
        bm.dataChanged()
    }

    fun getDayType(date: GregorianCalendar): Int {
        return dbMain.getEntryType(date)
    }

    fun getDaysList(year: Int, month: Int): ArrayList<CalendarCell> {

        val list = ArrayList<CalendarCell>()
        list.add(CalendarCell(year))

        val calendar = GregorianCalendar(year, month - 1, 1)

        var firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        firstDayOfWeek--
        if (firstDayOfWeek == 0) firstDayOfWeek = 7

        val daysCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        var i = 1
        while (i < firstDayOfWeek + daysCount) {

            val calendarCell = CalendarCell()

            if (i < firstDayOfWeek) {
                //adding empty 
            } else {
                calendarCell.day = i - firstDayOfWeek + 1
                calendarCell.month = month
                calendarCell.year = year

                val entry = dbMain.getEntry(calendar)

                if (entry != null) {

                    calendarCell.type = entry.type
                    calendarCell.dayofcycle = entry.dayofcycle
                    calendarCell.intensity = entry.intensity

                    for (s in entry.symptoms) {
                        if (s == 1) calendarCell.intercourse = true
                        else calendarCell.notes = true
                    }

                    if (entry.notes.isNotEmpty()) calendarCell.notes = true

                } else {
                    calendarCell.type = PeriodicalDatabase.DayEntry.EMPTY
                    calendarCell.dayofcycle = 0
                }
                calendar.add(GregorianCalendar.DATE, 1)
            }

            list.add(calendarCell)
            i++
        }
        return list
    }

}