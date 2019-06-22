package ru.korotkov.womencalendar.ui.steps

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import kotlinx.android.synthetic.main.activity_new_user.*
import ru.korotkov.womencalendar.Constants
import ru.korotkov.womencalendar.R
import ru.korotkov.womencalendar.app.CalendarApplication
import ru.korotkov.womencalendar.app.Dates
import ru.korotkov.womencalendar.ui.steps.slider.SliderAdapter
import ru.korotkov.womencalendar.ui.steps.slider.SliderLayoutManager
import ru.korotkov.womencalendar.ui.main.MainActivity


class StepsActivity : AppCompatActivity() {


    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var sliderLayoutManager: SliderLayoutManager

    private val inLengthMenstrualCycle = IntRange(3, 11)
    private val inLengthCycle = IntRange(15, 35)
    private val inBirthday = IntRange(1900, 2005)

    val snapHelper = LinearSnapHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        stepTopView.state
            .stepsNumber(4)
            .animationDuration(600)
            .commit()

        stepsNext.setOnClickListener {

        }

    }


    fun step(i: Int) {
        when (i) {
            0 -> {
                stepsRecycler.layoutManager = LinearLayoutManager(this)
                stepsRecycler.adapter = StepsCalendarAdapter(this, Dates.provideClearDates(3))

            }
        }
    }

    }
    fun firstStep() {
        textTitleNewUser.animate().alpha(1f).setDuration(100)

        sliderLayoutManager = SliderLayoutManager(this)
        recyclerNewUser.layoutManager = sliderLayoutManager
        sliderAdapter = SliderAdapter(firstStepArray)
        recyclerNewUser.adapter = sliderAdapter
        snapHelper.attachToRecyclerView(recyclerNewUser)

        new_user_next.setOnClickListener {

                v ->
            CalendarApplication.prefs.edit().putInt(
                Constants.LENGTH_MENSTRUAL_CYCLE,
                firstStepArray[snapHelper.getSnapPosition(recyclerNewUser) % firstStepArray.size]
            ).apply()


            step_view.go(1, true)
            textTitleNewUser.animate().alpha(0f).setDuration(1000).withEndAction {
                secondStep()
            }

        }
    }

    private fun secondStep() {
        textTitleNewUser.text = getText(R.string.new_user_question_1)
        textTitleNewUser.animate().alpha(1f).setDuration(1000)
        sliderAdapter = SliderAdapter(secondStepArray)
        recyclerNewUser.adapter = sliderAdapter
        new_user_next.setOnClickListener {

                v ->
            CalendarApplication.prefs.edit().putInt(
                Constants.LENGTH_FULL_CYCLE,
                secondStepArray[snapHelper.getSnapPosition(recyclerNewUser) % secondStepArray.size]
            ).apply()


            step_view.go(2, true)
            textTitleNewUser.animate().alpha(0f).setDuration(1000).withEndAction {
                thirdStep()
            }

        }
    }

    private fun thirdStep() {
        textTitleNewUser.text = "ВАШ ГОД РОЖДЕНИЯ"
        textTitleNewUser.animate().alpha(1f).duration = 1000
        sliderAdapter = SliderAdapter(thirdStepArray)
        recyclerNewUser.adapter = sliderAdapter
        new_user_next.setOnClickListener {

                v ->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            }
    }

    private fun stepFourth() {

    }

    fun onDateSelected() {

    }

}
