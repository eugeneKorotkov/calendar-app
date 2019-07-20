package com.vio.calendar.ui.more

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_cycle_settings.*

class CycleSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cycle_settings)

        toolbarCycleSettings.setNavigationOnClickListener {
                _ -> onBackPressed()
        }
        

        val preferences = PreferenceUtils(this)
        counterMenstrualLength.text = preferences.getInt("dates_range", 25).toString() + " " + getString(R.string.days)

        counterLengthCycle.text = (application as CalendarApplication).dbMain.getOption("period_length", 25).toString() + " " + getString(R.string.days)
    }
}
