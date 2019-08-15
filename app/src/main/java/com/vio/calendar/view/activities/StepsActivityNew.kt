package com.vio.calendar.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.data.user.UserRepository
import com.vio.calendar.ui.main.MainActivity
import com.vio.calendar.view.fragments.StepsCalendarFragment
import com.vio.calendar.view.fragments.UserInfoFragment
import kotlinx.android.synthetic.main.activity_steps.*

class StepsActivityNew : AppCompatActivity() {

    private val userInfoFragment = UserInfoFragment()
    private val stepsCalendarFragment = StepsCalendarFragment()
    private val userRepository = UserRepository()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_steps_new)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.stepsContainer, userInfoFragment).commit()

        stepsTopView.state
            .stepsNumber(3)
            .animationDuration(600)
            .commit()

        CalendarApplication.prefs.edit().putInt("startofweek", 1).apply()


      /*  stepsNext.setOnClickListener {
            val id = java.util.UUID.randomUUID().toString()
            val userData = UserData(editTextName.text.toString(), R.color.colorPink)
            val token = userRepository.getToken(User(id, "defPass!@S*ck", userData = userData))
            val preferences = PreferenceHelper.defaultPrefs(this)
            preferences.edit().putString("token", token).apply()
            Log.d("Easy", "token is : ${token}")
            goToMain()
        }*/


    }

    fun goToMain() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

    }
}
