package com.vio.calendar.view.fragments

import android.app.backup.BackupManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.db.PeriodicalDatabase
import com.vio.calendar.getMonthNameId
import com.vio.calendar.model.date.CalendarCell
import com.vio.calendar.setGestureListener
import com.vio.calendar.ui.calendar.CalendarFragment
import com.vio.calendar.ui.calendar.CalendarGridLayoutManager
import com.vio.calendar.ui.calendar.CalendarRecyclerAdapter
import com.vio.calendar.utils.AnimationHelper
import com.vio.calendar.view.activities.StepsActivityNew
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*
import kotlin.collections.ArrayList

class StepsCalendarFragment: Fragment() {

    private var firstIsShowing = true
    private var monthCurrent = GregorianCalendar().get(Calendar.MONTH) + 1

    private var yearCurrent = GregorianCalendar().get(Calendar.YEAR)

    private var date: GregorianCalendar? = null
    private var dateSecond: GregorianCalendar? = null

    private val calToday = GregorianCalendar()
    private val dayToday = calToday.get(GregorianCalendar.DATE)
    private val monthToday = calToday.get(GregorianCalendar.MONTH) + 1
    private val yearToday = calToday.get(GregorianCalendar.YEAR)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_steps_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendarRecyclerFirst.layoutManager = CalendarGridLayoutManager(context, 7)
        calendarRecyclerSecond.layoutManager = CalendarGridLayoutManager(context, 7)

        var weekDayNames = ArrayList<String>()

        weekDayNames.add(getString(R.string.mon).substring(0,1))
        weekDayNames.add(getString(R.string.tue).substring(0,1))
        weekDayNames.add(getString(R.string.wed).substring(0,1))
        weekDayNames.add(getString(R.string.thu).substring(0,1))
        weekDayNames.add(getString(R.string.fri).substring(0,1))
        weekDayNames.add(getString(R.string.sat).substring(0,1))
        weekDayNames.add(getString(R.string.sun).substring(0,1))

        val dayWeekAdapter = CalendarFragment.DayWeekAdapter(context!!, weekDayNames)

        weekDayNamesFirst.adapter = dayWeekAdapter
        weekDayNamesSecond.adapter = dayWeekAdapter

        calendarRecyclerSecond.isNestedScrollingEnabled = false
        calendarRecyclerFirst.isNestedScrollingEnabled = false


        firstConstraint.setGestureListener(
            {goNext()},
            {goPrev()}
        )

        secondConstraint.setGestureListener(
            {goNext()},
            {goPrev()}
        )

        initMonth()
        initCalendar()
    }

    private fun initMonth() {
        val cal = GregorianCalendar()
        monthCurrent = cal.get(Calendar.MONTH) + 1
        yearCurrent = cal.get(Calendar.YEAR)
    }

    private fun updateFirstCalendar() {
        val listFirst = ArrayList<CalendarCell>()
        val calFirst = GregorianCalendar(yearCurrent, monthCurrent - 1, 1)

        var firstDayOfWeekFirst = calFirst.get(Calendar.DAY_OF_WEEK)
        val daysCountFirst = calFirst.getActualMaximum(Calendar.DAY_OF_MONTH)

        val startOfWeek = CalendarApplication.preferences.getInt("startofweek", 0)

        if (startOfWeek == 1) {
            firstDayOfWeekFirst--
            if (firstDayOfWeekFirst == 0) firstDayOfWeekFirst = 7
        }


        monthNameFirst.text = getText(getMonthNameId(monthCurrent))

        var i = 1
        while (i < firstDayOfWeekFirst + daysCountFirst) {

            val calendarCell = CalendarCell()

            if (i < firstDayOfWeekFirst) {

            } else {
                calendarCell.day = i - firstDayOfWeekFirst + 1
                calendarCell.month = monthCurrent
                calendarCell.year = yearCurrent

                val entry = ((activity as StepsActivityNew).application as CalendarApplication).dbMain.getEntry(calFirst)

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

                if (calendarCell.day == dayToday &&
                    calendarCell.month == monthToday &&
                    calendarCell.year == yearToday) {

                    calendarCell.iscurrent = true

                }

                calFirst.add(GregorianCalendar.DATE, 1)
            }

            listFirst.add(calendarCell)
            i++
        }
        calendarRecyclerFirst.adapter = CalendarRecyclerAdapter(listFirst, firstDayOfWeekFirst, ({

            if (date != null && dateSecond != null) {
                date?.add(GregorianCalendar.DATE, -1)
                ((activity as StepsActivityNew).application as CalendarApplication).dbMain.removePeriod(date)
                handleDatabaseEdit()

                dateSecond?.add(GregorianCalendar.DATE, -1)
                ((activity as StepsActivityNew).application as CalendarApplication).dbMain.removePeriod(dateSecond)
                handleDatabaseEdit()

            }

            date = GregorianCalendar(yearCurrent, monthCurrent - 1, it.day)
            ((activity as StepsActivityNew).application as CalendarApplication).dbMain.addPeriod(date)
            handleDatabaseEdit()

            dateSecond = GregorianCalendar(yearCurrent, monthCurrent - 1, it.day)
            dateSecond?.add(GregorianCalendar.DATE, -(activity as StepsActivityNew).prefs.getInt("cycle_length", 25))

            ((activity as StepsActivityNew).application as CalendarApplication).dbMain.addPeriod(dateSecond)
            handleDatabaseEdit()

        }))
    }

    private fun updateSecondCalendar() {
        val listSecond = ArrayList<CalendarCell>()
        val calSecond = GregorianCalendar(yearCurrent, monthCurrent - 1, 1)

        var firstDayOfWeekSecond = calSecond.get(Calendar.DAY_OF_WEEK)

        val daysCountSecond = calSecond.getActualMaximum(Calendar.DAY_OF_MONTH)
        val startOfWeek = CalendarApplication.preferences.getInt("startofweek", 0)

        if (startOfWeek == 1) {
            firstDayOfWeekSecond--
            if (firstDayOfWeekSecond == 0) firstDayOfWeekSecond = 7
        }

        monthNameSecond.text = getText(getMonthNameId(monthCurrent))

        var i = 1
        while (i < firstDayOfWeekSecond + daysCountSecond) {

            val calendarCell = CalendarCell()

            if (i < firstDayOfWeekSecond) {

            } else {
                calendarCell.day = i - firstDayOfWeekSecond + 1
                calendarCell.month = monthCurrent
                calendarCell.year = yearCurrent

                val entry = ((activity as StepsActivityNew).application as CalendarApplication).dbMain.getEntry(calSecond)

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

                if (calendarCell.day == dayToday &&
                    calendarCell.month == monthToday &&
                    calendarCell.year == yearToday
                ) {

                    calendarCell.iscurrent = true
                }

                calSecond.add(GregorianCalendar.DATE, 1)
            }

            listSecond.add(calendarCell)
            i++
        }
        calendarRecyclerSecond.adapter = CalendarRecyclerAdapter(listSecond, firstDayOfWeekSecond, ({

            if (date != null && dateSecond != null) {
                date?.add(GregorianCalendar.DATE, -1)
                ((activity as StepsActivityNew).application as CalendarApplication).dbMain.removePeriod(date)
                handleDatabaseEdit()

                dateSecond?.add(GregorianCalendar.DATE, -1)
                ((activity as StepsActivityNew).application as CalendarApplication).dbMain.removePeriod(dateSecond)
                handleDatabaseEdit()

            }

            date = GregorianCalendar(yearCurrent, monthCurrent - 1, it.day)
            ((activity as StepsActivityNew).application as CalendarApplication).dbMain.addPeriod(date)
            handleDatabaseEdit()

            dateSecond = GregorianCalendar(yearCurrent, monthCurrent - 1, it.day)
            dateSecond?.add(GregorianCalendar.DATE, -(activity as StepsActivityNew).prefs.getInt("cycle_length", 25))

            ((activity as StepsActivityNew).application as CalendarApplication).dbMain.addPeriod(dateSecond)
            handleDatabaseEdit()

        }))
        calendarRecyclerSecond.invalidate()
    }

    private fun initCalendar() {
        Log.d("CalendarFragment", "init working: $firstIsShowing")
        firstIsShowing = if (firstIsShowing) {
            updateSecondCalendar()
                viewFlipper.showNext()
                false
        } else {
            updateFirstCalendar()
            true
        }
    }

    private fun goNext() {

        monthCurrent++
        if (monthCurrent > 12) {
            monthCurrent = 1
            yearCurrent++
        }

        firstIsShowing = if (firstIsShowing) {
            updateSecondCalendar()
            false
        } else {
            updateFirstCalendar()
            true
        }

        viewFlipper.inAnimation = AnimationHelper.inFromRightAnimation()
        viewFlipper.outAnimation = AnimationHelper.outToLeftAnimation()
        viewFlipper.showPrevious()
    }

    private fun goPrev() {
        // Update calendar
        // Update calendar
        monthCurrent--
        if (monthCurrent < 1) {
            monthCurrent = 12
            yearCurrent--
        }

        firstIsShowing = if (firstIsShowing) {
            updateSecondCalendar()
            false
        } else {
            updateFirstCalendar()
            true
        }

        viewFlipper.inAnimation = AnimationHelper.inFromLeftAnimation()
        viewFlipper.outAnimation = AnimationHelper.outToRightAnimation()
        viewFlipper.showNext()
    }

    private fun handleDatabaseEdit() {
        // Update calculated values
        ((activity as StepsActivityNew).application as CalendarApplication).dbMain.loadCalculatedData()
        if (firstIsShowing) {
            updateFirstCalendar()
        } else {
            updateSecondCalendar()
        }

        // Notify backup agent about the change and mark DB as clean
        val bm = BackupManager(CalendarApplication.getAppContext())
        bm.dataChanged()
    }

}