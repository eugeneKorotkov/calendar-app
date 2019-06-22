package ru.korotkov.womencalendar.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_splash.*
import ru.korotkov.womencalendar.Constants
import ru.korotkov.womencalendar.R
import ru.korotkov.womencalendar.app.CalendarApplication
import ru.korotkov.womencalendar.ui.license.LicenseActivity

class SplashActivity : AppCompatActivity() {

    val ANIMATION_DURATION: Long = 1000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        textTitle.animate().alpha(1f).setDuration(2000).withEndAction {
            if (CalendarApplication.prefs.getBoolean(Constants.LICENSE, false)) {
                val intent = Intent(this, LicenseActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, LicenseActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

}
