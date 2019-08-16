package com.vio.calendar.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vio.calendar.R
import kotlinx.android.synthetic.main.activity_steps.numberPicker
import kotlinx.android.synthetic.main.fragment_number_picker.*

class NumberPickerFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_number_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        numberPicker.setSelectorRoundedWrapPreferred(true)
        numberPicker.setWheelItemCount(5)
        numberPicker.setMax(11)
        numberPicker.setMin(3)
        numberPicker.setSelectedTextColor(R.color.colorPink)
    }

    fun switchToLength() {
        numberPicker.reset()
        numberPicker.setMax(35)
        numberPicker.setMin(21)
        numberPicker.reset()
        textNumberPicker.setText(R.string.steps_question_2)
    }


    fun switchToMenstrual() {
        numberPicker.reset()
        numberPicker.setMax(11)
        numberPicker.setMin(3)
        numberPicker.reset()
        textNumberPicker.setText(R.string.steps_question_1)
    }

    fun value(): Int = numberPicker.getCurrentItem().toInt()


}