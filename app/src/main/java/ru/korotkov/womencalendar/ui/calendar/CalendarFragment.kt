package ru.korotkov.womencalendar.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.korotkov.womencalendar.R

class CalendarFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*

        val list = ArrayList<Month>()

        val calendar = MenstrualCalendar(LocalDate.parse("2019-01-10"), 28, 5)


        //testing calendar #1
        calendar.getCalendarFrom(LocalDate.parse("2019-01-01"))
            .take(40)
            .forEach { println("${it.first} : ${it.second}") }

        //testing calendar #2
        calendar.getCalendarFrom(LocalDate.parse("2019-03-20"))
            .take(40)
            .forEach { println("${it.first} : ${it.second}") }


        //Creating screen version of the calendar

        val screenCalendar = MenstrualScreenCalendar(calendar)

        //generating required sequence for 12 month
        screenCalendar.getMenstrualCalendarScreenModels(2019, 4)
            .take(12)
            .forEach { list.add(it) }

        calendarRecyclerView.layoutManager = LinearLayoutManager(context)
        calendarRecyclerView.isNestedScrollingEnabled = false
        calendarRecyclerView.adapter = CalendarAdapter(list, context!!)

        */

    }

}
