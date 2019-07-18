package com.vio.calendar.ui.calendar

import android.app.AlertDialog
import android.app.backup.BackupManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.app.CalendarApplication.Companion.getAppContext
import com.vio.calendar.app.CalendarApplication.Companion.prefs
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

    private var listWithDates = ArrayList<CalendarCell>()

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
        return inflater.inflate(R.layout.fragment_calendar, container, false)
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

        viewFlipper.setGestureListener(
            {goNext()},
            {goPrev()}
        )

        initCalendar()
    }

    private fun updateFirstCalendar() {
        val listFirst = ArrayList<CalendarCell>()
        val calFirst = GregorianCalendar(yearCurrent, monthCurrent - 1, 1)
        var firstDayOfWeekFirst = calFirst.get(Calendar.DAY_OF_WEEK)
        val daysCountFirst = calFirst.getActualMaximum(Calendar.DAY_OF_MONTH)

        val startOfWeek = prefs.getInt("startofweek", 0)

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
                        }
                        OVULATION_PREDICTED, OVULATION_FUTURE -> {
                            headerCalendar.setBackgroundResource(R.drawable.rounded_linear_ovulation)
                            infoHeader.setTextColor(Color.WHITE)
                            titleHeader.setTextColor(Color.WHITE)
                            infoHeader.text = getText(R.string.ovulation_title)
                        }
                        PERIOD_START, PERIOD_PREDICTED, PERIOD_CONFIRMED -> {
                            headerCalendar.setBackgroundResource(R.drawable.rounded_linear_menstrual)
                            infoHeader.setTextColor(Color.WHITE)
                            titleHeader.setTextColor(Color.WHITE)
                            infoHeader.text = getText(R.string.menstrual_title)
                        }
                        INFERTILE_FUTURE, INFERTILE_PREDICTED -> {
                            headerCalendar.setBackgroundResource(R.drawable.rounded_linear_infertility)
                            infoHeader.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                            titleHeader.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                            infoHeader.text = getText(R.string.infertility_title)
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
                    titleHeader.setTextColor(Color.WHITE)
                    headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorFertilityHeader))
                    infoHeader.text = getText(R.string.fertility_title)
                }
                OVULATION_PREDICTED, OVULATION_FUTURE -> {
                    headerCalendar.setBackgroundResource(R.drawable.rounded_linear_ovulation)
                    infoHeader.setTextColor(Color.WHITE)
                    titleHeader.setTextColor(Color.WHITE)
                    headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorOvulationHeader))
                    infoHeader.text = getText(R.string.ovulation_title)
                }
                PERIOD_START, PERIOD_PREDICTED, PERIOD_CONFIRMED -> {
                    headerCalendar.setBackgroundResource(R.drawable.rounded_linear_menstrual)
                    infoHeader.setTextColor(Color.WHITE)
                    titleHeader.setTextColor(Color.WHITE)
                    headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorMenstrualHeader))
                    infoHeader.text = getText(R.string.menstrual_title)
                }
                INFERTILE_FUTURE, INFERTILE_PREDICTED -> {
                    headerCalendar.setBackgroundResource(R.drawable.rounded_linear_infertility)
                    infoHeader.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                    titleHeader.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                    headerButton.setTextColor(ContextCompat.getColor(context!!, android.R.color.white))
                    infoHeader.text = getText(R.string.infertility_title)
                }
            }


        }), ({

            val builder = AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
            val date = GregorianCalendar(yearCurrent, monthCurrent - 1, it.day)
            val type = ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
            if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                builder.setMessage(
                    resources.getString(
                        R.string.calendaraction_add
                    )
                )
                builder.setPositiveButton(
                    resources.getString(R.string.calendaraction_ok)
                ) { _, _ ->
                    ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                    handleDatabaseEdit()
                }

                builder.setNegativeButton(
                    resources.getString(R.string.calendaraction_cancel)
                ) { _, _ -> }

                builder.setNeutralButton(
                    resources.getString(R.string.calendaraction_details)
                ) { dialog, which -> (activity as MainActivity).showDetailsActivity(yearCurrent, monthCurrent, it.day) }
            } else {
                if (type == PERIOD_START)
                    builder.setMessage(resources.getString(R.string.calendaraction_removeperiod))
                else
                    builder.setMessage(resources.getString(R.string.calendaraction_remove))
                builder.setPositiveButton(
                    resources.getString(R.string.calendaraction_ok)
                ) { _, _ ->
                    ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                    handleDatabaseEdit()
                }

                builder.setNegativeButton(
                    resources.getString(R.string.calendaraction_cancel)
                ) { _, _ -> }

                builder.setNeutralButton(
                    resources.getString(R.string.calendaraction_details)
                ) { _, _ ->
                    (activity as MainActivity).showDetailsActivity(yearCurrent, monthCurrent, it.day) }
            }
            builder.show()
            true
        }))
        calendarRecyclerFirst.invalidate()
    }

    private fun updateSecondCalendar() {
        val listSecond = ArrayList<CalendarCell>()
        val calSecond = GregorianCalendar(yearCurrent, monthCurrent - 1, 1)

        var firstDayOfWeekSecond = calSecond.get(Calendar.DAY_OF_WEEK)

        val daysCountSecond = calSecond.getActualMaximum(Calendar.DAY_OF_MONTH)
        val startOfWeek = prefs.getInt("startofweek", 0)

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
                        }
                        OVULATION_PREDICTED, OVULATION_FUTURE -> {
                            headerCalendar.setBackgroundResource(R.drawable.rounded_linear_ovulation)
                            infoHeader.setTextColor(Color.WHITE)
                            titleHeader.setTextColor(Color.WHITE)
                            infoHeader.text = getText(R.string.ovulation_title)
                        }
                        PERIOD_START, PERIOD_PREDICTED, PERIOD_CONFIRMED -> {
                            headerCalendar.setBackgroundResource(R.drawable.rounded_linear_menstrual)
                            infoHeader.setTextColor(Color.WHITE)
                            titleHeader.setTextColor(Color.WHITE)
                            infoHeader.text = getText(R.string.menstrual_title)
                        }
                        INFERTILE_FUTURE, INFERTILE_PREDICTED -> {
                            headerCalendar.setBackgroundResource(R.drawable.rounded_linear_infertility)
                            infoHeader.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                            titleHeader.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                            infoHeader.text = getText(R.string.infertility_title)
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
                    titleHeader.setTextColor(Color.WHITE)
                    headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorFertilityHeader))
                    infoHeader.text = getText(R.string.fertility_title)
                }
                OVULATION_PREDICTED, OVULATION_FUTURE -> {
                    headerCalendar.setBackgroundResource(R.drawable.rounded_linear_ovulation)
                    infoHeader.setTextColor(Color.WHITE)
                    titleHeader.setTextColor(Color.WHITE)
                    headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorOvulationHeader))
                    infoHeader.text = getText(R.string.ovulation_title)
                }
                PERIOD_START, PERIOD_PREDICTED, PERIOD_CONFIRMED -> {
                    headerCalendar.setBackgroundResource(R.drawable.rounded_linear_menstrual)
                    infoHeader.setTextColor(Color.WHITE)
                    titleHeader.setTextColor(Color.WHITE)
                    headerButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorMenstrualHeader))
                    infoHeader.text = getText(R.string.menstrual_title)
                }
                INFERTILE_FUTURE, INFERTILE_PREDICTED -> {
                    headerCalendar.setBackgroundResource(R.drawable.rounded_linear_infertility)
                    infoHeader.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                    titleHeader.setTextColor(ContextCompat.getColor(context!!, R.color.colorRedMonthName))
                    headerButton.setTextColor(ContextCompat.getColor(context!!, android.R.color.white))
                    infoHeader.text = getText(R.string.infertility_title)
                }
            }
        }), ({

            val builder = AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
            val date = GregorianCalendar(yearCurrent, monthCurrent - 1, it.day)
            val type = ((activity as MainActivity).application as CalendarApplication).dbMain.getEntryType(date)
            if (type != PERIOD_START && type != PERIOD_CONFIRMED) {
                builder.setMessage(
                    resources.getString(
                        R.string.calendaraction_add
                    )
                )
                builder.setPositiveButton(
                    resources.getString(R.string.calendaraction_ok)
                ) { _, _ ->
                    ((activity as MainActivity).application as CalendarApplication).dbMain.addPeriod(date)
                    handleDatabaseEdit()
                }

                builder.setNegativeButton(
                    resources.getString(R.string.calendaraction_cancel)
                ) { _, _ -> }

                builder.setNeutralButton(
                    resources.getString(R.string.calendaraction_details)
                ) { dialog, which -> (activity as MainActivity).showDetailsActivity(yearCurrent, monthCurrent, it.day) }
            } else {
                if (type == PERIOD_START)
                    builder.setMessage(resources.getString(R.string.calendaraction_removeperiod))
                else
                    builder.setMessage(resources.getString(R.string.calendaraction_remove))
                builder.setPositiveButton(
                    resources.getString(R.string.calendaraction_ok)
                ) { _, _ ->
                    ((activity as MainActivity).application as CalendarApplication).dbMain.removePeriod(date)
                    handleDatabaseEdit()
                }

                builder.setNegativeButton(
                    resources.getString(R.string.calendaraction_cancel)
                ) { _, _ -> }

                builder.setNeutralButton(
                    resources.getString(R.string.calendaraction_details)
                ) { _, _ ->
                    (activity as MainActivity).showDetailsActivity(yearCurrent, monthCurrent, it.day) }
            }
            builder.show()
            true
        }))
        calendarRecyclerSecond.invalidate()
    }

    private fun initCalendar() {
        updateFirstCalendar()
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
        initCalendar()

        // Notify backup agent about the change and mark DB as clean
        val bm = BackupManager(getAppContext())
        bm.dataChanged()


    }



    class DayWeekAdapter: BaseAdapter {
        var namesDayWeek = ArrayList<String>()
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
