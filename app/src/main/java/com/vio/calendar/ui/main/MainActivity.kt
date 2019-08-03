package com.vio.calendar.ui.main

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.google.android.gms.ads.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vio.calendar.R
import com.vio.calendar.setTransparentStatusBar
import com.vio.calendar.ui.articles.ArticlesFragment
import com.vio.calendar.ui.calendar.CalendarFragment
import com.vio.calendar.ui.prefs.PrefsFragment


class MainActivity : LocalizationActivity() {

    private var par = true

    private var adsCounter = 1

    private val articlesFragment = ArticlesFragment()
    private val todayFragment = CalendarFragment()
    private val prefsFragment = PrefsFragment()

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
                switchToFragment(prefsFragment)
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

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        MobileAds.initialize(this, "ca-app-pub-1890073619173649~8908748583")

        loadSplashAd()
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY_SECOND)

        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY_THIRD)


        mInterstitialAdScreens = InterstitialAd(this)
        mInterstitialAdScreens.adUnitId = "ca-app-pub-1890073619173649/8078882648"
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


    private fun loadSplashAd() {

        mAdSplash = InterstitialAd(this)
        mAdSplash.adUnitId = "ca-app-pub-1890073619173649/2395311128"
        mAdSplash.loadAd(AdRequest.Builder().build())

    }


    private val mRunnable: Runnable = Runnable {
        if (mAdSplash.isLoaded && par) {
            par = false
            mAdSplash.show()
        }
    }
}

