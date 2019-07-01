package com.vio.womencalendar.ui.main

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vio.womencalendar.R
import com.vio.womencalendar.setTransparentStatusBar
import com.vio.womencalendar.ui.articles.ArticlesFragment
import com.vio.womencalendar.ui.calendar.CalendarFragment
import com.vio.womencalendar.ui.today.TodayFragment

class MainActivity : AppCompatActivity() {

    private val articlesFragment = ArticlesFragment()
    private val todayFragment = TodayFragment()
    private val calendarFragment = CalendarFragment()
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {

            R.id.navigation_today -> {
                switchToFragment(todayFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_calendar -> {
                switchToFragment(calendarFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_advices -> {
                switchToFragment(articlesFragment)
                return@OnNavigationItemSelectedListener true

            }
            R.id.navigation_more -> {
                switchToFragment(articlesFragment)
                return@OnNavigationItemSelectedListener true

            }
        }
        false
    }

    private fun switchToFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frame, fragment).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setTransparentStatusBar()
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        switchToFragment(articlesFragment)
    }
}
