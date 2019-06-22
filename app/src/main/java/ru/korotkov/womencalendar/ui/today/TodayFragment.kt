package ru.korotkov.womencalendar.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_today.*
import ru.korotkov.womencalendar.R
import ru.korotkov.womencalendar.model.Day
import ru.korotkov.womencalendar.model.MenstrualCalendar
import ru.korotkov.womencalendar.model.MenstrualScreenCalendar
import ru.korotkov.womencalendar.model.Month
import java.time.LocalDate
import java.util.*


class TodayFragment : Fragment() {

    val weeks = ArrayList<String>()

    private lateinit var adapter: TodayRecyclerAdapter

    companion object {
        fun newInstance() = TodayFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)



        val nameOfDaysAdapter = MonthAdapter(context!!, weeks)
        gridView.adapter = nameOfDaysAdapter

        val arrayList = ArrayList<Day>()

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
        val list: List<Month>
        val screenCalendar = MenstrualScreenCalendar(calendar)


        val todayRecyclerAdapter = TodayRecyclerAdapter(arrayList)

        todayRecycler.layoutManager = GridLayoutManager(context, 7)
        todayRecycler.adapter = todayRecyclerAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        weeks.clear()
        weeks.add("п")
        weeks.add("в")
        weeks.add("с")
        weeks.add("ч")
        weeks.add("п")
        weeks.add("с")
        weeks.add("в")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false)

    }




}
