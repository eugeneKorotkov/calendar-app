package com.vio.calendar.view.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.vio.calendar.PreferenceHelper
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.data.user.UserRepository
import com.vio.calendar.data.user.model.User
import com.vio.calendar.data.user.model.UserData
import com.vio.calendar.ui.main.MainActivity
import com.vio.calendar.view.fragments.NumberPickerFragmentLength
import com.vio.calendar.view.fragments.NumberPickerFragmentMenstrual
import com.vio.calendar.view.fragments.StepsCalendarFragment
import com.vio.calendar.view.fragments.UserInfoFragment
import kotlinx.android.synthetic.main.activity_steps_new.*
import java.util.*

class StepsActivity : LocalizationActivity() {

    private var databaseHelper = CalendarApplication.getDatabaseHelper()


    private val userInfoFragment = UserInfoFragment()
    private val stepsCalendarFragment = StepsCalendarFragment()
    private val numberPickerFragmentLength = NumberPickerFragmentLength()
    private val numberPickerFragmentMenstrual = NumberPickerFragmentMenstrual()
    private lateinit var userRepository: UserRepository
    lateinit var prefs: SharedPreferences

    private var stepCounter = 0

    private val colorList = listOf(
        "#101357",
        "#51d0de",
        "#0f2862",
        "#6B7A8F",
        "#F7882F",
        "#F7C331",
        "#DCC7AA",
        "#1561ad",
        "#1c77ac",
        "#1dbab4",
        "#fc5226",
        "#eb1736",
        "#8bf0ba",
        "#94f0f1",
        "#f2b1d8",
        "#e62739",
        "#3a4660",
        "#ed8a63"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_steps_new)

        userRepository = UserRepository(this)

        prefs = PreferenceHelper.defaultPrefs(applicationContext)


        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.stepsContainer, userInfoFragment).commit()

        stepsTopView.state
            .stepsNumber(4)
            .animationDuration(600)
            .commit()

        toolbar.setNavigationOnClickListener {
            if (stepCounter > 0) stepCounter--
            when (stepCounter) {
                0 -> {
                    stepsTopView.go(0, true)
                    switchToFragment(userInfoFragment)
                }
                1 -> {
                    stepsTopView.go(1, true)
                    switchToFragment(numberPickerFragmentMenstrual)
                }
                2 -> {
                    stepsTopView.go(2, true)
                    switchToFragment(numberPickerFragmentLength)
                }
            }
        }

        stepsNext.setOnClickListener {
            if (stepCounter < 4) stepCounter++
            when (stepCounter) {
                1 -> {
                    if (userInfoFragment.getName().length >= 4) {
                        hideBottomPanel()
                        stepsTopView.go(1, true)
                        prefs.edit().putString("user_name", userInfoFragment.getName()).apply()
                        val color = colorList.random()
                        prefs.edit().putString("color", colorList.random()).apply()
                        userRepository.getToken(
                            User(
                                UUID.randomUUID().toString(),
                                "defOsucy",
                                UserData(userInfoFragment.getName(), color)
                            )
                        )
                        switchToFragment(numberPickerFragmentMenstrual)
                    } else {
                        Toast.makeText(this, getString(R.string.errorName), Toast.LENGTH_SHORT).show()
                        stepCounter--
                    }

                }
                2 -> {
                    stepsTopView.go(2, true)
                    prefs.edit().putInt("period_length", numberPickerFragmentMenstrual.value()).apply()
                    databaseHelper.setOption("period_length", numberPickerFragmentMenstrual.value())
                    switchToFragment(numberPickerFragmentLength)
                }
                3 -> {
                    stepsTopView.go(3, true)
                    prefs.edit().putInt("cycle_length", numberPickerFragmentLength.value()).apply()
                    switchToFragment(stepsCalendarFragment)
                }

                4 -> {
                    if (prefs.getBoolean("license", false)) {
                        goToMain()
                    } else {
                        Toast.makeText(this, getString(R.string.errorDate), Toast.LENGTH_SHORT).show()
                        stepCounter--
                    }
                }

            }
        }

    }

    override fun onResume() {
        super.onResume()
        hideBottomPanel()
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun switchToFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.stepsContainer, fragment).commit()
    }

    private fun hideBottomPanel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
    }


}
