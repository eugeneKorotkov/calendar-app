package ru.korotkov.womencalendar.ui.license

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_license.*
import ru.korotkov.womencalendar.Constants
import ru.korotkov.womencalendar.R
import ru.korotkov.womencalendar.app.CalendarApplication
import ru.korotkov.womencalendar.ui.steps.StepsActivity

class LicenseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_license)

        radioButton1.text = getText(R.string.license_checkbox_0)
        radioButton2.text = getText(R.string.license_checkbox_1)
        radioButton3.text = getText(R.string.license_checkbox_2)
        radioButton4.text = getText(R.string.license_checkbox_3)

        radioButton1.movementMethod = LinkMovementMethod.getInstance()
        radioButton2.movementMethod = LinkMovementMethod.getInstance()
        radioButton3.movementMethod = LinkMovementMethod.getInstance()
        radioButton4.movementMethod = LinkMovementMethod.getInstance()

        radioButton1.setOnCheckedChangeListener { buttonView, isChecked -> isAllCheckBoxes() }
        radioButton2.setOnCheckedChangeListener { buttonView, isChecked -> isAllCheckBoxes() }
        radioButton3.setOnCheckedChangeListener { buttonView, isChecked -> isAllCheckBoxes() }
        radioButton4.setOnCheckedChangeListener { buttonView, isChecked -> isAllCheckBoxes() }

        button_accept_all.setOnClickListener {
            radioButton1.isChecked = true
            radioButton2.isChecked = true
            radioButton3.isChecked = true
            radioButton4.isChecked = true
        }

        button_next.setOnClickListener {
            CalendarApplication.prefs.edit().putBoolean(Constants.LICENSE, true).apply()
            val intent = Intent(this, StepsActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun isAllCheckBoxes() {
        if (radioButton1.isChecked && radioButton2.isChecked
            && radioButton3.isChecked && radioButton4.isChecked)  {
            button_next.isClickable = true
            button_next.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            button_next.setBackgroundResource(R.drawable.rounded_button)
            button_accept_all.visibility = View.GONE
        } else if (button_next.isClickable){
            button_next.isClickable = false
            button_next.setBackgroundResource(android.R.color.transparent)
        }
    }

}