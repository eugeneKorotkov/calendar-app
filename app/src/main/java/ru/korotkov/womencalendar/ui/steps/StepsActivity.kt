package ru.korotkov.womencalendar.ui.steps

import android.animation.LayoutTransition
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import kotlinx.android.synthetic.main.activity_steps.*
import ru.korotkov.womencalendar.R
import ru.korotkov.womencalendar.app.Dates
import ru.korotkov.womencalendar.show
import ru.korotkov.womencalendar.ui.steps.slider.SliderAdapter
import ru.korotkov.womencalendar.ui.steps.slider.SliderLayoutManager


class StepsActivity : AppCompatActivity() {


    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var sliderLayoutManager: SliderLayoutManager

    private val inLengthMenstrualCycle: IntArray = IntRange(3, 11).toList().toIntArray()
    private val inLengthCycle: IntArray = IntRange(15, 35).toList().toIntArray()
    private val inBirthday: IntArray = IntRange(1900, 2005).toList().toIntArray()

    private val snapHelper = LinearSnapHelper()
    private var stepCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_steps)

        (stepsBottomContainer as ViewGroup).layoutTransition.enableTransitionType(LayoutTransition.CHANGING);

        sliderLayoutManager = SliderLayoutManager(this)

        stepsTopView.state
            .stepsNumber(4)
            .animationDuration(600)
            .commit()

        step(stepCounter)

        stepsSkip.setOnClickListener {

        }

        stepsNext.setOnClickListener {
            stepCounter++
            step(stepCounter)
        }

        toolbar.setNavigationOnClickListener {
            if (stepCounter > 0) {
                stepCounter--
                step(stepCounter)
            }
        }
    }
    //TODO fix too much code

    private fun step(i: Int) {
        when (i) {
            0 -> {
                stepsTitle.show()
                stepsTopView.go(0, true)
                stepsRecycler.layoutManager = LinearLayoutManager(this)

                stepsRecycler.adapter = StepsCalendarAdapter(this, Dates.provideClearDates(3)) {
                    Log.d("StepsActivity", "clicked: " + it.getYear() + ", " + it.getMonth() + ", " + it.getDay() )
                }
            }
            1 -> {
                stepsTitle.text = getText(R.string.new_user_question_1)
                stepsRecycler.layoutManager = sliderLayoutManager
                sliderAdapter = SliderAdapter(inLengthMenstrualCycle)
                stepsRecycler.adapter = sliderAdapter
                snapHelper.attachToRecyclerView(stepsRecycler)
                stepsTopView.go(1, true)
            }

            2 -> {
                stepsTitle.text = getText(R.string.new_user_question_2)
                sliderAdapter = SliderAdapter(inLengthCycle)
                stepsRecycler.adapter = sliderAdapter
                stepsTopView.go(2, true)
            }
            3 -> {
                stepsTitle.text = getText(R.string.new_user_question_3)
                sliderAdapter = SliderAdapter(inBirthday)
                stepsRecycler.adapter = sliderAdapter
                stepsTopView.go(3, true)
            }
        }
    }

}
