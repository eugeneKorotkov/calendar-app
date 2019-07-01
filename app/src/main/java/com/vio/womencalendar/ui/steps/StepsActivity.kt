package com.vio.womencalendar.ui.steps

import android.animation.LayoutTransition
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.vio.womencalendar.R
import com.vio.womencalendar.app.Dates
import com.vio.womencalendar.model.preferences.UserInfoPreferences
import com.vio.womencalendar.model.user.UserInfo
import com.vio.womencalendar.show
import com.vio.womencalendar.ui.steps.slider.SliderAdapter
import com.vio.womencalendar.ui.steps.slider.SliderLayoutManager
import kotlinx.android.synthetic.main.activity_steps.*


class StepsActivity : AppCompatActivity() {


    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var sliderLayoutManager: SliderLayoutManager

    private val inLengthMenstrualCycle: IntArray = IntRange(3, 11).toList().toIntArray()
    private val inLengthCycle: IntArray = IntRange(15, 35).toList().toIntArray()
    private val inBirthday: IntArray = IntRange(1900, 2005).toList().toIntArray()

    private val snapHelper = LinearSnapHelper()
    private var stepCounter = 0

    private var userInfo = UserInfo(4)

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

        stepsNext.setOnClickListener {
            stepCounter++
            step(stepCounter)

            if (stepCounter == 4) {
                UserInfoPreferences.updateUserInfo(userInfo)
            }

        }

        toolbar.setNavigationOnClickListener {
            stepCounter--
            step(stepCounter)
        }
    }
    //TODO fix too much code

    private fun step(i: Int) {
        when (i) {
            0 -> {
                stepsTitle.show()
                stepsTitle.text = getText(R.string.new_user_question_0)
                stepsTopView.go(0, true)
                stepsRecycler.layoutManager = LinearLayoutManager(this)
                stepsRecycler.setFadingEdgeLength(0)
                stepsRecycler.adapter = StepsCalendarAdapter(this, Dates.provideClearDates(3)) {
                    Log.d("StepsActivity", "clicked: " + it.getYear() + ", " + it.getMonth() + ", " + it.getDay())
                }
            }
            1 -> {
                stepsRecycler.setFadingEdgeLength(100)
                stepsTitle.text = getText(R.string.new_user_question_1)
                stepsRecycler.layoutManager = sliderLayoutManager
                sliderAdapter = SliderAdapter(inLengthMenstrualCycle) {
                    userInfo.lengthMenstrual = it
                }
                stepsRecycler.adapter = sliderAdapter
                snapHelper.attachToRecyclerView(stepsRecycler)
                stepsTopView.go(1, true)
            }

            2 -> {
                stepsTitle.text = getText(R.string.new_user_question_2)
                sliderAdapter = SliderAdapter(inLengthCycle) {
                    userInfo.lengthCycle = it
                }
                stepsRecycler.adapter = sliderAdapter
                stepsTopView.go(2, true)
            }
            3 -> {
                stepsTitle.text = getText(R.string.new_user_question_3)
                sliderAdapter = SliderAdapter(inBirthday) {
                    userInfo.birthDate = it
                }
                stepsRecycler.adapter = sliderAdapter
                stepsTopView.go(3, true)
            }
        }
    }

}
