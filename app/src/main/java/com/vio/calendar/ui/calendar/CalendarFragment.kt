package com.vio.calendar.ui.calendar

import android.app.backup.BackupManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.app.CalendarApplication.Companion.getAppContext
import com.vio.calendar.app.CalendarApplication.Companion.preferences
import com.vio.calendar.db.PeriodicalDatabase.DayEntry.*
import com.vio.calendar.getMonthNameForTitle
import com.vio.calendar.getMonthNameId
import com.vio.calendar.model.date.CalendarCell
import com.vio.calendar.setGestureListener
import com.vio.calendar.ui.main.MainActivity
import com.vio.calendar.utils.AnimationHelper
import kotlinx.android.synthetic.main.day_item_row.view.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*
import kotlin.collections.ArrayList


class CalendarFragment : Fragment() {

    private var firstIsShowing = true

    private var monthCurrent = GregorianCalendar().get(Calendar.MONTH) + 1
    private var yearCurrent = GregorianCalendar().get(Calendar.YEAR)

    private val calToday = GregorianCalendar()
    private val dayToday = calToday.get(GregorianCalendar.DATE)
    private val monthToday = calToday.get(GregorianCalendar.MONTH) + 1
    private val yearToday = calToday.get(GregorianCalendar.YEAR)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        view.setGestureListener({goNext()}, {goPrev()})
        return view
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
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

        val dayWeekAdapter = DayWeekAdapter(context!!, weekDayNames)

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
        handleDatabaseEdit()
        initCalendar()
        handleDatabaseEdit()

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

        val startOfWeek = preferences.getInt("startofweek", 0)

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

                val entry = ((activity as MainActivity).application as CalendarApplication).dbMain.getEntry(calFirst)

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
                    calendarCell.type = EMPTY
                    calendarCell.dayofcycle = 0
                }

                if (calendarCell.day == dayToday &&
                    calendarCell.month == monthToday &&
                    calendarCell.year == yearToday) {

                    calendarCell.iscurrent = true
                    titleHeader.text = getText(R.string.title_today)

                    when (calendarCell.type) {
                        FERTILITY_PREDICTED, FERTILITY_FUTURE -> {
                            headerCalendar.setBackgroundResource(R.drawable.rounded_linear_fertility)
                            infoHeader.setTextColor(Color.WHITE)
                            titleHeader.setTextColor(Color.WHITE)
                            infoHeader.text = getText(R.string.fertility_title)
                            headerButton.text = getText(R.string.check_menstrual)
                            headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorFertilityHeader))
                            var day = calendarCell.day
                            headerButton.setOnClickListener {
                                val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                                val type =
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                                if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                                    handleDatabaseEdit()
                                } else {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                                    handleDatabaseEdit()
                                }
                            }
                        }
                        OVULATION_PREDICTED, OVULATION_FUTURE -> {
                            headerCalendar.setBackgroundResource(R.drawable.rounded_linear_ovulation)
                            infoHeader.setTextColor(Color.WHITE)
                            titleHeader.setTextColor(Color.WHITE)
                            infoHeader.text = getText(R.string.ovulation_title)
                            headerButton.text = getText(R.string.check_menstrual)
                            headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorOvulationHeader))
                            var day = calendarCell.day
                            headerButton.setOnClickListener {
                                val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                                val type =
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                                if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                                    handleDatabaseEdit()
                                } else {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                                    handleDatabaseEdit()
                                }
                            }
                        }
                        PERIOD_PREDICTED -> {
                            headerCalendar.setBackgroundResource(R.drawable.rounded_linear_menstrual)
                            infoHeader.setTextColor(Color.WHITE)
                            titleHeader.setTextColor(Color.WHITE)
                            infoHeader.text = getText(R.string.menstrual_title)

                            headerButton.text = getText(R.string.check_menstrual)
                            headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorMenstrualHeader))
                            var day = calendarCell.day
                            headerButton.setOnClickListener {
                                val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                                val type =
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                                if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                                    handleDatabaseEdit()
                                } else {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                                    handleDatabaseEdit()
                                }
                            }
                        }
                        PERIOD_START, PERIOD_CONFIRMED -> {
                            headerCalendar.setBackgroundResource(R.drawable.rounded_linear_menstrual)
                            infoHeader.setTextColor(Color.WHITE)
                            titleHeader.setTextColor(Color.WHITE)
                            infoHeader.text = getText(R.string.menstrual_title)
                            headerButton.text = getText(R.string.delete_menstrual)
                            headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorMenstrualHeader))
                            var day = calendarCell.day
                            headerButton.setOnClickListener {
                                val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                                val type =
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                                if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                                    handleDatabaseEdit()
                                } else {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                                    handleDatabaseEdit()
                                }
                            }
                        }
                        INFERTILE_FUTURE, INFERTILE_PREDICTED -> {
                            headerCalendar.setBackgroundResource(R.drawable.rounded_linear_infertility)
                            infoHeader.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                            titleHeader.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                            infoHeader.text = getText(R.string.infertility_title)
                            headerButton.text = getText(R.string.check_menstrual)
                            headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                            var day = calendarCell.day
                            headerButton.setOnClickListener {
                                val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                                val type =
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                                if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                                    handleDatabaseEdit()
                                } else {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                                    handleDatabaseEdit()
                                }
                            }
                        }
                        EMPTY -> {
                            headerCalendar.setBackgroundResource(R.drawable.rounded_linear_red)
                            infoHeader.setTextColor(ContextCompat.getColor(context!!, android.R.color.white))
                            titleHeader.setTextColor(ContextCompat.getColor(context!!, android.R.color.white))
                            infoHeader.text = getText(R.string.empty_title)
                            headerButton.text = getText(R.string.check_menstrual)
                            headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.transparentRed))
                            var day = calendarCell.day
                            headerButton.setOnClickListener {
                                val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                                val type =
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                                if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                                    handleDatabaseEdit()
                                } else {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                                    handleDatabaseEdit()
                                }
                            }
                        }

                    }

                }

                calFirst.add(GregorianCalendar.DATE, 1)
            }

            listFirst.add(calendarCell)
            i++
        }
        calendarRecyclerFirst.adapter = CalendarRecyclerAdapter(listFirst, firstDayOfWeekFirst, ({

            titleHeader.text = it.day.toString() + " " + getText(getMonthNameForTitle(monthCurrent))


            when (it.type) {
                FERTILITY_PREDICTED, FERTILITY_FUTURE -> {
                    headerCalendar.setBackgroundResource(R.drawable.rounded_linear_fertility)
                    infoHeader.setTextColor(Color.WHITE)
                    infoHeader.text = getText(R.string.fertility_title)
                    titleHeader.setTextColor(Color.WHITE)
                    headerButton.text = getText(R.string.check_menstrual)
                    headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorFertilityHeader))
                    var day = it.day
                    headerButton.setOnClickListener {
                        val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                        val type =
                            ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                        if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                            handleDatabaseEdit()
                        } else {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                            handleDatabaseEdit()
                        }
                    }
                }
                OVULATION_PREDICTED, OVULATION_FUTURE -> {
                    headerCalendar.setBackgroundResource(R.drawable.rounded_linear_ovulation)
                    infoHeader.setTextColor(Color.WHITE)
                    infoHeader.text = getText(R.string.ovulation_title)
                    titleHeader.setTextColor(Color.WHITE)
                    headerButton.text = getText(R.string.check_menstrual)
                    headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorOvulationHeader))
                    var day = it.day
                    headerButton.setOnClickListener {
                        val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                        val type =
                            ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                        if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                            handleDatabaseEdit()
                        } else {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                            handleDatabaseEdit()
                        }
                    }
                }
                PERIOD_PREDICTED -> {
                    headerCalendar.setBackgroundResource(R.drawable.rounded_linear_menstrual)
                    infoHeader.setTextColor(Color.WHITE)
                    titleHeader.setTextColor(Color.WHITE)
                    infoHeader.text = getText(R.string.menstrual_title)

                    headerButton.text = getText(R.string.check_menstrual)
                    headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorMenstrualHeader))
                    var day = it.day
                    headerButton.setOnClickListener {
                        val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                        val type =
                            ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                        if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                            handleDatabaseEdit()
                        } else {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                            handleDatabaseEdit()
                        }
                    }
                }

                PERIOD_START, PERIOD_CONFIRMED -> {
                    headerCalendar.setBackgroundResource(R.drawable.rounded_linear_menstrual)
                    infoHeader.setTextColor(Color.WHITE)
                    titleHeader.setTextColor(Color.WHITE)
                    infoHeader.text = getText(R.string.menstrual_title)
                    headerButton.text = getText(R.string.delete_menstrual)
                    headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorMenstrualHeader))
                    var day = it.day
                    headerButton.setOnClickListener {
                        val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                        val type =
                            ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                        if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                            handleDatabaseEdit()
                        } else {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                            handleDatabaseEdit()
                        }
                    }
                }
                INFERTILE_FUTURE, INFERTILE_PREDICTED -> {
                    headerCalendar.setBackgroundResource(R.drawable.rounded_linear_infertility)
                    infoHeader.text = getText(R.string.infertility_title)
                    infoHeader.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                    titleHeader.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                    headerButton.text = getText(R.string.check_menstrual)
                    headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                    var day = it.day
                    headerButton.setOnClickListener {
                        val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                        val type =
                            ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                        if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                            handleDatabaseEdit()
                        } else {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                            handleDatabaseEdit()
                        }
                    }
                }
                EMPTY -> {
                    headerCalendar.setBackgroundResource(R.drawable.rounded_linear_red)
                    infoHeader.setTextColor(ContextCompat.getColor(context!!, android.R.color.white))
                    titleHeader.setTextColor(ContextCompat.getColor(context!!, android.R.color.white))
                    infoHeader.text = getText(R.string.empty_title)
                    headerButton.text = getText(R.string.check_menstrual)
                    headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.transparentRed))
                    var day = it.day
                    headerButton.setOnClickListener {
                        val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                        val type =
                            ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                        if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                            handleDatabaseEdit()
                        } else {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                            handleDatabaseEdit()
                        }
                    }
                }
            }
            calendarRecyclerFirst.invalidate()

        }))/*, {
            Log.d("CalendarFragment", "addPeriod: $it")
            ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(it)
            handleDatabaseEdit()
        })*/
    }

    private fun updateSecondCalendar() {
        val listSecond = ArrayList<CalendarCell>()
        val calSecond = GregorianCalendar(yearCurrent, monthCurrent - 1, 1)

        var firstDayOfWeekSecond = calSecond.get(Calendar.DAY_OF_WEEK)

        val daysCountSecond = calSecond.getActualMaximum(Calendar.DAY_OF_MONTH)
        val startOfWeek = preferences.getInt("startofweek", 0)

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

                val entry = ((activity as MainActivity).application as CalendarApplication).dbMain.getEntry(calSecond)

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
                    calendarCell.type = EMPTY
                    calendarCell.dayofcycle = 0
                }

                if (calendarCell.day == dayToday &&
                    calendarCell.month == monthToday &&
                    calendarCell.year == yearToday) {

                    calendarCell.iscurrent = true
                    titleHeader.text = getText(R.string.title_today)

                    when (calendarCell.type) {
                        FERTILITY_PREDICTED, FERTILITY_FUTURE -> {
                            headerCalendar.setBackgroundResource(R.drawable.rounded_linear_fertility)
                            infoHeader.setTextColor(Color.WHITE)
                            titleHeader.setTextColor(Color.WHITE)
                            infoHeader.text = getText(R.string.fertility_title)
                            headerButton.text = getText(R.string.check_menstrual)
                            headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorFertilityHeader))
                            var day = calendarCell.day
                            headerButton.setOnClickListener {
                                val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                                val type =
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                                if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                                    handleDatabaseEdit()
                                } else {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                                    handleDatabaseEdit()
                                }
                            }
                        }
                        OVULATION_PREDICTED, OVULATION_FUTURE -> {
                            headerCalendar.setBackgroundResource(R.drawable.rounded_linear_ovulation)
                            infoHeader.setTextColor(Color.WHITE)
                            titleHeader.setTextColor(Color.WHITE)
                            infoHeader.text = getText(R.string.ovulation_title)
                            headerButton.text = getText(R.string.check_menstrual)
                            headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorOvulationHeader))
                            var day = calendarCell.day
                            headerButton.setOnClickListener {
                                val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                                val type =
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                                if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                                    handleDatabaseEdit()
                                } else {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                                    handleDatabaseEdit()
                                }
                            }
                        }
                        PERIOD_PREDICTED -> {
                            headerCalendar.setBackgroundResource(R.drawable.rounded_linear_menstrual)
                            infoHeader.setTextColor(Color.WHITE)
                            titleHeader.setTextColor(Color.WHITE)
                            infoHeader.text = getText(R.string.menstrual_title)

                            headerButton.text = getText(R.string.check_menstrual)
                            headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorMenstrualHeader))
                            var day = calendarCell.day
                            headerButton.setOnClickListener {
                                val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                                val type =
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                                if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                                    handleDatabaseEdit()
                                } else {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                                    handleDatabaseEdit()
                                }
                            }
                        }

                        PERIOD_START, PERIOD_CONFIRMED -> {
                            headerCalendar.setBackgroundResource(R.drawable.rounded_linear_menstrual)
                            infoHeader.setTextColor(Color.WHITE)
                            titleHeader.setTextColor(Color.WHITE)
                            infoHeader.text = getText(R.string.menstrual_title)
                            headerButton.text = getText(R.string.delete_menstrual)
                            headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorMenstrualHeader))
                            var day = calendarCell.day
                            headerButton.setOnClickListener {
                                val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                                val type =
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                                if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                                    handleDatabaseEdit()
                                } else {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                                    handleDatabaseEdit()
                                }
                            }
                        }
                        INFERTILE_FUTURE, INFERTILE_PREDICTED -> {
                            headerCalendar.setBackgroundResource(R.drawable.rounded_linear_infertility)
                            infoHeader.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                            titleHeader.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                            infoHeader.text = getText(R.string.infertility_title)
                            headerButton.text = getText(R.string.check_menstrual)
                            headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                            var day = calendarCell.day
                            headerButton.setOnClickListener {
                                val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                                val type =
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                                if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                                    handleDatabaseEdit()
                                } else {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                                    handleDatabaseEdit()
                                }
                            }
                        }
                        EMPTY -> {
                            headerCalendar.setBackgroundResource(R.drawable.rounded_linear_red)
                            infoHeader.setTextColor(ContextCompat.getColor(context!!, android.R.color.white))
                            titleHeader.setTextColor(ContextCompat.getColor(context!!, android.R.color.white))
                            infoHeader.text = getText(R.string.empty_title)
                            headerButton.text = getText(R.string.check_menstrual)
                            headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.transparentRed))
                            var day = calendarCell.day
                            headerButton.setOnClickListener {
                                val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                                val type =
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                                if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                                    handleDatabaseEdit()
                                } else {
                                    ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                                    handleDatabaseEdit()
                                }
                            }
                        }

                    }
                }

                calSecond.add(GregorianCalendar.DATE, 1)
            }

            listSecond.add(calendarCell)
            i++
        }
        calendarRecyclerSecond.adapter = CalendarRecyclerAdapter(listSecond, firstDayOfWeekSecond, ({

            titleHeader.text = it.day.toString() + " " + getText(getMonthNameForTitle(monthCurrent))


            when (it.type) {
                FERTILITY_PREDICTED, FERTILITY_FUTURE -> {
                    headerCalendar.setBackgroundResource(R.drawable.rounded_linear_fertility)
                    infoHeader.setTextColor(Color.WHITE)
                    infoHeader.text = getText(R.string.fertility_title)
                    titleHeader.setTextColor(Color.WHITE)
                    headerButton.text = getText(R.string.check_menstrual)
                    headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorFertilityHeader))
                    var day = it.day
                    headerButton.setOnClickListener {
                        val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                        val type =
                            ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                        if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                            handleDatabaseEdit()
                        } else {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                            handleDatabaseEdit()
                        }
                    }
                }
                OVULATION_PREDICTED, OVULATION_FUTURE -> {
                    headerCalendar.setBackgroundResource(R.drawable.rounded_linear_ovulation)
                    infoHeader.setTextColor(Color.WHITE)
                    infoHeader.text = getText(R.string.ovulation_title)
                    titleHeader.setTextColor(Color.WHITE)
                    headerButton.text = getText(R.string.check_menstrual)
                    headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorOvulationHeader))
                    var day = it.day
                    headerButton.setOnClickListener {
                        val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                        val type =
                            ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                        if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                            handleDatabaseEdit()
                        } else {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                            handleDatabaseEdit()
                        }
                    }
                }
                PERIOD_PREDICTED -> {
                    headerCalendar.setBackgroundResource(R.drawable.rounded_linear_menstrual)
                    infoHeader.setTextColor(Color.WHITE)
                    titleHeader.setTextColor(Color.WHITE)
                    infoHeader.text = getText(R.string.menstrual_title)

                    headerButton.text = getText(R.string.check_menstrual)
                    headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorMenstrualHeader))
                    var day = it.day
                    headerButton.setOnClickListener {
                        val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                        val type =
                            ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                        if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                            handleDatabaseEdit()
                        } else {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                            handleDatabaseEdit()
                        }
                    }
                }

                PERIOD_START, PERIOD_CONFIRMED -> {
                    headerCalendar.setBackgroundResource(R.drawable.rounded_linear_menstrual)
                    infoHeader.setTextColor(Color.WHITE)
                    titleHeader.setTextColor(Color.WHITE)
                    infoHeader.text = getText(R.string.menstrual_title)

                    headerButton.text = getText(R.string.delete_menstrual)
                    headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorMenstrualHeader))
                    var day = it.day
                    headerButton.setOnClickListener {
                        val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                        val type =
                            ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                        if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                            handleDatabaseEdit()
                        } else {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                            handleDatabaseEdit()
                        }
                    }
                }
                INFERTILE_FUTURE, INFERTILE_PREDICTED -> {
                    headerCalendar.setBackgroundResource(R.drawable.rounded_linear_infertility)
                    infoHeader.text = getText(R.string.infertility_title)
                    infoHeader.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                    titleHeader.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                    headerButton.text = getText(R.string.check_menstrual)
                    headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                    var day = it.day
                    headerButton.setOnClickListener {
                        val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                        val type =
                            ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                        if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                            handleDatabaseEdit()
                        } else {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                            handleDatabaseEdit()
                        }
                    }
                }
                EMPTY -> {
                    headerCalendar.setBackgroundResource(R.drawable.rounded_linear_red)
                    infoHeader.setTextColor(ContextCompat.getColor(context!!, android.R.color.white))
                    titleHeader.setTextColor(ContextCompat.getColor(context!!, android.R.color.white))
                    infoHeader.text = getText(R.string.empty_title)
                    headerButton.text = getText(R.string.check_menstrual)
                    headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.transparentRed))
                    var day = it.day
                    headerButton.setOnClickListener {
                        val date = GregorianCalendar(yearCurrent, monthCurrent - 1, day)
                        val type =
                            ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
                        if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                            handleDatabaseEdit()
                        } else {
                            ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                            handleDatabaseEdit()
                        }
                    }
                }
            }
        }))
        calendarRecyclerSecond.invalidate()
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
        ((activity as MainActivity).application as CalendarApplication).dbMain.loadCalculatedData()
        if (firstIsShowing) {
            updateFirstCalendar()
        } else {
            updateSecondCalendar()
        }

        // Notify backup agent about the change and mark DB as clean
        val bm = BackupManager(getAppContext())
        bm.dataChanged()


    }



    class DayWeekAdapter: BaseAdapter {
        private var namesDayWeek = ArrayList<String>()
        var context: Context? = null

        constructor(context: Context, list: ArrayList<String>) : super() {
            this.context = context
            this.namesDayWeek = list
        }

        override fun getCount(): Int {
            return namesDayWeek.size
        }

        override fun getItem(position: Int): Any {
            return namesDayWeek[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val name = this.namesDayWeek[position]
            val inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflator.inflate(R.layout.day_item_row, null)
            view.dayName.text = name
            return view
        }
    }



}
