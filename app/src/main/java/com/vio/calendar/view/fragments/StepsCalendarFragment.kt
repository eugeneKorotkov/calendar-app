package com.vio.calendar.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.getMonthNameForTitle
import com.vio.calendar.setGestureListener
import com.vio.calendar.ui.calendar.CalendarGridLayoutManager
import com.vio.calendar.utils.AnimationHelper
import com.vio.calendar.view.activities.StepsActivity
import com.vio.calendar.view.adapters.CalendarRecyclerAdapter
import com.vio.calendar.view.adapters.DayWeekAdapter
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*
import kotlin.collections.ArrayList

class StepsCalendarFragment: Fragment() {

    private var firstIsShowing = true

    private var databaseHelper = CalendarApplication.getDatabaseHelper()
    private var calToday = GregorianCalendar()

    private var date: GregorianCalendar? = null
    private var dateSecond: GregorianCalendar? = null

    private var monthCurrent = GregorianCalendar().get(Calendar.MONTH) + 1
    private var yearCurrent = GregorianCalendar().get(Calendar.YEAR)
    private var firstDayOfWeek = 1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_steps_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDayNames()
        calculateFirstDayOfWeek()

        calendarRecyclerFirst.layoutManager = CalendarGridLayoutManager(context, 7)
        calendarRecyclerSecond.layoutManager = CalendarGridLayoutManager(context, 7)

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

        initCalendar()
    }


    private fun updateFirstCalendar() {
        val list = databaseHelper.getDaysList(yearCurrent, monthCurrent)
        monthNameFirst.text = getString(getMonthNameForTitle(monthCurrent))

        calendarRecyclerFirst.adapter = CalendarRecyclerAdapter(list, firstDayOfWeek) {
            addPeriod(it.day)
            updateFirstCalendar()
        }
    }

    private fun updateSecondCalendar() {
        val list = databaseHelper.getDaysList(yearCurrent, monthCurrent)
        monthNameSecond.text = getString(getMonthNameForTitle(monthCurrent))

        calendarRecyclerSecond.adapter = CalendarRecyclerAdapter(list, firstDayOfWeek) {
            addPeriod(it.day)
            updateSecondCalendar()
        }
    }

    private fun initCalendar() {

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

        calculateFirstDayOfWeek()

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

        monthCurrent--
        if (monthCurrent < 1) {
            monthCurrent = 12
            yearCurrent--
        }

        calculateFirstDayOfWeek()

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

    private fun initDayNames() {
        var weekDayNames = ArrayList<String>()

        weekDayNames.add(getString(R.string.mon).substring(0,1))
        weekDayNames.add(getString(R.string.tue).substring(0,1))
        weekDayNames.add(getString(R.string.wed).substring(0,1))
        weekDayNames.add(getString(R.string.thu).substring(0,1))
        weekDayNames.add(getString(R.string.fri).substring(0,1))
        weekDayNames.add(getString(R.string.sat).substring(0,1))
        weekDayNames.add(getString(R.string.sun).substring(0,1))

        val dayWeekAdapter = DayWeekAdapter(context!!, weekDayNames)

        weekDayNamesFirst.adapter = dayWeekAdapter
        weekDayNamesSecond.adapter = dayWeekAdapter
    }

    private fun calculateFirstDayOfWeek() {
        firstDayOfWeek = GregorianCalendar(yearCurrent, monthCurrent - 1, 1).get(Calendar.DAY_OF_WEEK)
        firstDayOfWeek--
        if (firstDayOfWeek == 0) firstDayOfWeek = 7
    }

    private fun addPeriod(day: Int) {

        if (date != null && dateSecond != null) {
            date?.add(GregorianCalendar.DATE, -1)
            databaseHelper.removePeriod(date!!)
            dateSecond?.add(GregorianCalendar.DATE, -1)
            databaseHelper.removePeriod(dateSecond!!)
        }

        date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
        databaseHelper.addPeriod(date!!)

        dateSecond = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
        dateSecond?.add(GregorianCalendar.DATE, (activity as StepsActivity).prefs.getInt("cycle_length", 25))
        databaseHelper.addPeriod(dateSecond!!)

        (activity as StepsActivity).prefs.edit().putBoolean("license", true).apply()
    }

}