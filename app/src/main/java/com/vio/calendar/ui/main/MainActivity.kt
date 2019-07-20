package com.vio.calendar.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vio.calendar.R
import com.vio.calendar.preferences.PreferenceActivity
import com.vio.calendar.setTransparentStatusBar
import com.vio.calendar.ui.ListActivity
import com.vio.calendar.ui.articles.ArticlesFragment
import com.vio.calendar.ui.calendar.CalendarFragment
import com.vio.calendar.ui.details.DetailsActivity
import com.vio.calendar.ui.more.MoreActivity


class MainActivity : AppCompatActivity() {

    companion object {
        const val SET_OPTIONS = 2
        const val MORE_ACTIVITY = 3
        const val DETAILS_CLOSED = 5  // Details: closed

    }
    private var par = true

    private var adsCounter = 1

    private val articlesFragment = ArticlesFragment()
    private val todayFragment = CalendarFragment()

    lateinit var mAdView : AdView

    private lateinit var mAdSplash: InterstitialAd

    private lateinit var mInterstitialAdScreens: InterstitialAd
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds
    private val SPLASH_DELAY_SECOND: Long = 5000 //5 seconds
    private val SPLASH_DELAY_THIRD: Long = 10000 //10 seconds


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {

            R.id.navigation_today -> {
                switchToFragment(todayFragment)
                return@OnNavigationItemSelectedListener true

            }
            R.id.navigation_advices -> {
                switchToFragment(articlesFragment)
                return@OnNavigationItemSelectedListener true

            }
            R.id.navigation_more -> {
                showMoreActivity()
                return@OnNavigationItemSelectedListener true

            }
        }
        false
    }

    private fun switchToFragment(fragment: Fragment) {
        adsCounter++
        if (adsCounter % 5 == 1) {
            if (mInterstitialAdScreens.isLoaded) {
                mInterstitialAdScreens.show()
            }
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frame, fragment).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setTransparentStatusBar()
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this, "ca-app-pub-1890073619173649~8908748583")

        loadSplashAd()
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY_SECOND)

        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY_THIRD)


        mInterstitialAdScreens = InterstitialAd(this)
        mInterstitialAdScreens.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAdScreens.adListener = object : AdListener() {
            override fun onAdClosed() {
                mInterstitialAdScreens.loadAd(AdRequest.Builder().build())
            }
        }
        mInterstitialAdScreens.loadAd(AdRequest.Builder().build())


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = ContextCompat.getColor(applicationContext, R.color.transparent)
        }


        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        switchToFragment(todayFragment)

    }

    private fun showOptions() {
        startActivityForResult(
            Intent(this@MainActivity, PreferenceActivity::class.java), SET_OPTIONS)
    }


    fun showDetailsActivity(year: Int, month: Int, day: Int) {
        val details = Intent(this@MainActivity, DetailsActivity::class.java)
        details.putExtra("year", year)
        details.putExtra("month", month)
        details.putExtra("day", day)
        startActivityForResult(details, DETAILS_CLOSED)
    }

    private fun loadSplashAd() {

        mAdSplash = InterstitialAd(this)
        mAdSplash.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mAdSplash.loadAd(AdRequest.Builder().build())

    }

    private fun showMoreActivity() {
        startActivityForResult(
            Intent(this@MainActivity, MoreActivity::class.java), 1
        )
    }

    private fun showList() {
        startActivityForResult(
            Intent(this@MainActivity, ListActivity::class.java), 1
        )
    }

    private val mRunnable: Runnable = Runnable {
        if (mAdSplash.isLoaded && par) {
            par = false
            mAdSplash.show()
        }
    }
}

