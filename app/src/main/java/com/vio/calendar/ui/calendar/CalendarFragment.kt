package com.vio.calendar.ui.calendar

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.db.PeriodicalDatabase.DayEntry.*
import com.vio.calendar.getMonthNameForTitle
import com.vio.calendar.model.date.CalendarCell
import com.vio.calendar.setGestureListener
import com.vio.calendar.utils.AnimationHelper
import com.vio.calendar.view.adapters.CalendarRecyclerAdapter
import com.vio.calendar.view.adapters.DayWeekAdapter
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*
import kotlin.collections.ArrayList


class CalendarFragment : Fragment() {

    private var firstIsShowing = true

    private var databaseHelper = CalendarApplication.getDatabaseHelper()
    private var calToday = GregorianCalendar()

    private var monthCurrent = calToday.get(Calendar.MONTH) + 1
    private var yearCurrent = calToday.get(Calendar.YEAR)
    private var dayCurrent = calToday.get(Calendar.DATE)
    private var firstDayOfWeek = 1

    private lateinit var calendarCellCurrent: CalendarCell


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        view.setGestureListener({ goNext() }, { goPrev() })
        return view
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        initDayNames()
        calculateFirstDayOfWeek()
        headerButton.setOnClickListener {
            if (calendarCellCurrent.type != PERIOD_CONFIRMED && calendarCellCurrent.type != PERIOD_START) {
                databaseHelper.addPeriod(
                    GregorianCalendar(yearCurrent, monthCurrent - 1, calendarCellCurrent.day)
                )
            } else {
                databaseHelper.removePeriod(GregorianCalendar(yearCurrent, monthCurrent - 1, calendarCellCurrent.day))
            }
            headerSet()
            if (firstIsShowing) updateFirstCalendar() else updateSecondCalendar()
        }

        calendarRecyclerFirst.layoutManager = CalendarGridLayoutManager(context, 7)
        calendarRecyclerSecond.layoutManager = CalendarGridLayoutManager(context, 7)

        calendarRecyclerSecond.isNestedScrollingEnabled = false
        calendarRecyclerFirst.isNestedScrollingEnabled = false


        firstConstraint.setGestureListener(
            { goNext() },
            { goPrev() }
        )

        secondConstraint.setGestureListener(
            { goNext() },
            { goPrev() }
        )

        initCalendar()

        var type = databaseHelper.getDayType(
            GregorianCalendar(
                yearCurrent, monthCurrent - 1, dayCurrent
            )
        )
        calendarCellCurrent = CalendarCell(type = type, year = yearCurrent, month = monthCurrent, day = dayCurrent)

        headerSet()

    }


    private fun headerSet() {

        titleHeader.text = calendarCellCurrent.day.toString() + " " + getText(getMonthNameForTitle(monthCurrent))

        Log.d("CalendarFragment", "clicked calendar cell with: ${calendarCellCurrent.type}")

        when (calendarCellCurrent.type) {
            FERTILITY_PREDICTED, FERTILITY_FUTURE -> {
                headerCalendar.setBackgroundResource(R.drawable.rounded_linear_fertility)
                infoHeader.setTextColor(Color.WHITE)
                titleHeader.setTextColor(Color.WHITE)
                infoHeader.text = getText(R.string.fertility_title)
                headerButton.text = getText(R.string.check_menstrual)
                headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorFertilityHeader))
            }
            OVULATION_PREDICTED, OVULATION_FUTURE -> {
                headerCalendar.setBackgroundResource(R.drawable.rounded_linear_ovulation)
                infoHeader.setTextColor(Color.WHITE)
                titleHeader.setTextColor(Color.WHITE)
                infoHeader.text = getText(R.string.ovulation_title)
                headerButton.text = getText(R.string.check_menstrual)
                headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorOvulationHeader))

            }
            PERIOD_PREDICTED -> {
                headerCalendar.setBackgroundResource(R.drawable.rounded_linear_menstrual)
                infoHeader.setTextColor(Color.WHITE)
                titleHeader.setTextColor(Color.WHITE)
                infoHeader.text = getText(R.string.menstrual_title)
                headerButton.text = getText(R.string.check_menstrual)
                headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorMenstrualHeader))
            }

            PERIOD_START, PERIOD_CONFIRMED -> {
                headerCalendar.setBackgroundResource(R.drawable.rounded_linear_menstrual)
                infoHeader.setTextColor(Color.WHITE)
                titleHeader.setTextColor(Color.WHITE)
                infoHeader.text = getText(R.string.menstrual_title)
                headerButton.text = getText(R.string.delete_menstrual)
                headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorMenstrualHeader))
            }
            INFERTILE_FUTURE, INFERTILE_PREDICTED -> {
                headerCalendar.setBackgroundResource(R.drawable.rounded_linear_infertility)
                infoHeader.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                titleHeader.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                infoHeader.text = getText(R.string.infertility_title)
                headerButton.text = getText(R.string.check_menstrual)
                headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
            }
            EMPTY -> {
                headerCalendar.setBackgroundResource(R.drawable.rounded_linear_red)
                infoHeader.setTextColor(ContextCompat.getColor(context!!, android.R.color.white))
                titleHeader.setTextColor(ContextCompat.getColor(context!!, android.R.color.white))
                infoHeader.text = getText(R.string.empty_title)
                headerButton.text = getText(R.string.check_menstrual)
                headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.transparentRed))
            }
        }
    }

    private fun updateFirstCalendar() {
        val list = databaseHelper.getDaysList(yearCurrent, monthCurrent)
        monthNameFirst.text = getString(getMonthNameForTitle(monthCurrent))

        calendarRecyclerFirst.adapter = CalendarRecyclerAdapter(list, firstDayOfWeek) {
            calendarCellCurrent = it
            dayCurrent = it.day
            headerSet()
        }
    }

    private fun updateSecondCalendar() {
        val list = databaseHelper.getDaysList(yearCurrent, monthCurrent)
        monthNameSecond.text = getString(getMonthNameForTitle(monthCurrent))

        calendarRecyclerSecond.adapter = CalendarRecyclerAdapter(list, firstDayOfWeek) {
            calendarCellCurrent = it
            dayCurrent = it.day
            headerSet()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("firstIsShowing", firstIsShowing)
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
        // Update calendar
        // Update calendar
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

        weekDayNames.add(getString(R.string.mon).substring(0, 1))
        weekDayNames.add(getString(R.string.tue).substring(0, 1))
        weekDayNames.add(getString(R.string.wed).substring(0, 1))
        weekDayNames.add(getString(R.string.thu).substring(0, 1))
        weekDayNames.add(getString(R.string.fri).substring(0, 1))
        weekDayNames.add(getString(R.string.sat).substring(0, 1))
        weekDayNames.add(getString(R.string.sun).substring(0, 1))

        val dayWeekAdapter = DayWeekAdapter(context!!, weekDayNames)

        weekDayNamesFirst.adapter = dayWeekAdapter
        weekDayNamesSecond.adapter = dayWeekAdapter
    }

    private fun calculateFirstDayOfWeek() {
        firstDayOfWeek = GregorianCalendar(yearCurrent, monthCurrent - 1, 1).get(Calendar.DAY_OF_WEEK)
        firstDayOfWeek--
        if (firstDayOfWeek == 0) firstDayOfWeek = 7
    }

}
