package com.vio.calendar.ui.steps

import android.app.backup.BackupManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.BaseAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.db.PeriodicalDatabase
import com.vio.calendar.getMonthNameId
import com.vio.calendar.model.date.CalendarCell
import com.vio.calendar.show
import com.vio.calendar.ui.main.MainActivity
import com.vio.calendar.utils.PreferenceUtils
import com.vio.calendar.view.adapters.StepsRecyclerAdapter
import kotlinx.android.synthetic.main.activity_steps.*
import kotlinx.android.synthetic.main.weekday_name.view.*
import java.util.*


class StepsActivity : LocalizationActivity() {

    private var monthCurrent = GregorianCalendar().get(Calendar.MONTH) + 1
    private var yearCurrent = GregorianCalendar().get(Calendar.YEAR)

    private var datesRange = 28

    private var stepCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_steps)



        stepsTopView.state
            .stepsNumber(3)
            .animationDuration(600)
            .commit()

        CalendarApplication.prefs.edit().putInt("startofweek", 1).apply()


        step(stepCounter)

        stepsNext.setOnClickListener {
            stepCounter++
            step(stepCounter)

            if (stepCounter == 3) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

        toolbar.setNavigationOnClickListener {
            stepCounter--
            step(stepCounter)
        }
    }

    private fun step(i: Int) {
        when (i) {
            0 -> {
                stepsTitle.show()
                //stepsTitle.text = getText(R.string.new_user_question_1)
                stepsTopView.go(0, true)

                numberPicker.setSelectorRoundedWrapPreferred(true)
                numberPicker.setWheelItemCount(8)
                numberPicker.setMax(11)
                numberPicker.setMin(3)
                numberPicker.setSelectedTextColor(R.color.colorMainGreen)

            }
            1 -> {

                numberPicker.visibility = View.VISIBLE
                linearSteps.visibility = View.GONE

                (application as CalendarApplication).dbMain.setOption("period_length", numberPicker.getCurrentItem())
                handleDatabaseEdit()
                (application as CalendarApplication).initDatabase()


                Log.d("StepsActivity", "put in preferences period_length ${numberPicker.getCurrentItem()}")
                numberPicker.reset()
                numberPicker.setMax(35)
                numberPicker.setMin(21)
                numberPicker.reset()

                //stepsTitle.text = getText(R.string.new_user_question_2)
                stepsTopView.go(1, true)
            }

            2 -> {

                val gesture = GestureDetector(
                    this,
                    object : GestureDetector.SimpleOnGestureListener() {

                        override fun onDown(e: MotionEvent): Boolean {
                            return true
                        }

                        override fun onFling(
                            e1: MotionEvent, e2: MotionEvent, velocityX: Float,
                            velocityY: Float
                        ): Boolean {
                            val SWIPE_MIN_DISTANCE = 120
                            val SWIPE_MAX_OFF_PATH = 250
                            val SWIPE_THRESHOLD_VELOCITY = 200
                            try {
                                if (Math.abs(e1.y - e2.y) > SWIPE_MAX_OFF_PATH)
                                    return false
                                if (e1.x - e2.x > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                    Log.d("CalendarFragment", "goNext")
                                    goNext()
                                } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                    Log.d("CalendarFragment", "goPrev")
                                    goPrev()
                                }
                            } catch (e: Exception) {
                                // nothing
                            }

                            return super.onFling(e1, e2, velocityX, velocityY)
                        }
                    })

                linearSteps.setOnTouchListener { v, event -> gesture.onTouchEvent(event) }

                datesRange = numberPicker.getCurrentItem().toInt()
                numberPicker.visibility = View.GONE
                linearSteps.visibility = View.VISIBLE

                var nameDayWeekList = ArrayList<String>()
                nameDayWeekList.clear()
                nameDayWeekList.add(getString(R.string.mon).substring(0,1))
                nameDayWeekList.add(getString(R.string.tue).substring(0,1))
                nameDayWeekList.add(getString(R.string.wed).substring(0,1))
                nameDayWeekList.add(getString(R.string.thu).substring(0,1))
                nameDayWeekList.add(getString(R.string.fri).substring(0,1))
                nameDayWeekList.add(getString(R.string.sat).substring(0,1))
                nameDayWeekList.add(getString(R.string.sun).substring(0,1))

                gridViewSteps.adapter = DayWeekAdapter(this!!, nameDayWeekList)

                //stepsTitle.text = getText(R.string.new_user_question_0)
                stepsTopView.go(2, true)
                stepsRecycler.layoutManager = GridLayoutManager(this, 7)
                calendarUpdate()
            }
        }
    }

    private fun handleDatabaseEdit() {
        // Update calculated values
        (application as CalendarApplication).dbMain.loadCalculatedData()
        // Notify backup agent about the change and mark DB as clean
        val bm = BackupManager(CalendarApplication.getAppContext())
        bm.dataChanged()
    }


    private fun calendarUpdate() {
        val list = ArrayList<CalendarCell>()
        val cal = GregorianCalendar(yearCurrent, monthCurrent - 1, 1)

        var firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val daysCount = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val startOfWeek = CalendarApplication.prefs.getInt("startofweek", 0)

        if (startOfWeek == 1) {
            firstDayOfWeek--
            if (firstDayOfWeek == 0) firstDayOfWeek = 7
        }

        val calToday = GregorianCalendar()
        val dayToday = calToday.get(GregorianCalendar.DATE)
        val monthToday = calToday.get(GregorianCalendar.MONTH) + 1
        val yearToday = calToday.get(GregorianCalendar.YEAR)

        stepsMonthName.text = getText(getMonthNameId(monthCurrent))

        var i = 1
        while (i < firstDayOfWeek + daysCount) {

            val calendarCell = CalendarCell()

            if (i < firstDayOfWeek) {

            } else {
                calendarCell.day = i - firstDayOfWeek + 1
                calendarCell.month = monthCurrent
                calendarCell.year = yearCurrent

                val entry = (application as CalendarApplication).dbMain.getEntry(cal)

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

                cal.add(GregorianCalendar.DATE, 1)
            }

            list.add(calendarCell)
            i++
        }
        stepsRecycler.adapter = StepsRecyclerAdapter(list, firstDayOfWeek) {
            val date = GregorianCalendar(yearCurrent, monthCurrent - 1, it.day)
            Log.d("StepsActivity", "i tried to mark: $date")
            (application as CalendarApplication).dbMain.addPeriod(date)

            val preferences = PreferenceUtils(this)
            preferences.edit().putInt("dates_range", datesRange).apply()

            val dateSecond = GregorianCalendar(yearCurrent, monthCurrent - 1, it.day)
            dateSecond.add(GregorianCalendar.DATE, datesRange)
            Log.d("StepsActivity", "i tried to mark: $dateSecond")

            (application as CalendarApplication).dbMain.addPeriod(dateSecond)

            handleDatabaseEdit()
        }
        stepsRecycler.invalidate()
    }

    fun goNext() {
        // Update calendar
        monthCurrent++
        if (monthCurrent > 12) {
            monthCurrent = 1
            yearCurrent++
        }

        calendarUpdate()
    }

    fun goPrev() {
        // Update calendar
        monthCurrent--
        if (monthCurrent < 1) {
            monthCurrent = 12
            yearCurrent--
        }

        calendarUpdate()
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
            val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.weekday_name, null)
            view.dayWeekName.text = name
            return view
        }
    }


}
