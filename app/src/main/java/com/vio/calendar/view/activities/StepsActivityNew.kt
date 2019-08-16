package com.vio.calendar.view.activities

import android.app.backup.BackupManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vio.calendar.PreferenceHelper
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.data.user.UserRepository
import com.vio.calendar.ui.main.MainActivity
import com.vio.calendar.view.fragments.NumberPickerFragment
import com.vio.calendar.view.fragments.StepsCalendarFragment
import com.vio.calendar.view.fragments.UserInfoFragment
import kotlinx.android.synthetic.main.activity_steps.*

class StepsActivityNew : AppCompatActivity() {

    private val userInfoFragment = UserInfoFragment()
    private val stepsCalendarFragment = StepsCalendarFragment()
    private val numberPickerFragment = NumberPickerFragment()
    private val userRepository = UserRepository()
    lateinit var prefs: SharedPreferences

    private var stepCounter = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_steps_new)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        prefs = PreferenceHelper.defaultPrefs(this)


        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.stepsContainer, userInfoFragment).commit()

        stepsTopView.state
            .stepsNumber(4)
            .animationDuration(600)
            .commit()

        CalendarApplication.preferences.edit().putInt("startofweek", 1).apply()

        toolbar.setNavigationOnClickListener {
            if (stepCounter > 0) stepCounter--
            when (stepCounter) {
                0 -> {
                    stepsTopView.go(0, true)
                    switchToFragment(userInfoFragment)
                }
                1 -> {
                    stepsTopView.go(1, true)
                    numberPickerFragment.switchToMenstrual()
                }
                2 -> {
                    switchToFragment(numberPickerFragment)
                        //numberPickerFragment.switchToLength()
                }
            }
        }

        stepsNext.setOnClickListener {
            if (stepCounter < 4) stepCounter++
            when (stepCounter) {
                1 -> {
                    stepsTopView.go(1, true)
                    prefs.edit().putString("user_name", userInfoFragment.getName()).apply()
                    switchToFragment(numberPickerFragment)
                }
                2 -> {
                    stepsTopView.go(2, true)
                    prefs.edit().putInt("period_length", numberPickerFragment.value()).apply()
                    (application as CalendarApplication).dbMain.setOption("period_length", numberPickerFragment.value())
                    handleDatabaseEdit()
                    (application as CalendarApplication).initDatabase()
                    numberPickerFragment.switchToLength()
                }
                3 -> {
                    stepsTopView.go(3, true)
                    prefs.edit().putInt("cycle_length", numberPickerFragment.value()).apply()

                    switchToFragment(stepsCalendarFragment)
                }
                4 -> {
                    goToMain()
                }

            }
        }

    }

    fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun switchToFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.stepsContainer, fragment).commit()
    }

    private fun handleDatabaseEdit() {
        // Update calculated values
        (application as CalendarApplication).dbMain.loadCalculatedData()
        // Notify backup agent about the change and mark DB as clean
        val bm = BackupManager(CalendarApplication.getAppContext())
        bm.dataChanged()
    }


}
