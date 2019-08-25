package com.vio.calendar.ui.main

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.akexorcist.localizationactivity.core.LanguageSetting.getLanguage
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.google.android.gms.ads.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vio.calendar.PreferenceHelper.defaultPrefs
import com.vio.calendar.R
import com.vio.calendar.data.user.UserRepository
import com.vio.calendar.data.user.model.UserData
import com.vio.calendar.model.dialog.CycleItem
import com.vio.calendar.model.dialog.LanguageItem
import com.vio.calendar.model.dialog.NotificationItem
import com.vio.calendar.setTransparentStatusBar
import com.vio.calendar.ui.articles.ArticlesFragment
import com.vio.calendar.ui.calendar.CalendarFragment
import com.vio.calendar.view.adapters.DialogLanguageAdapter
import com.vio.calendar.view.adapters.DialogNotificationAdapter
import com.vio.calendar.view.fragments.PrefsFragment
import com.yarolegovich.lovelydialog.LovelyChoiceDialog
import com.yarolegovich.lovelydialog.LovelyCustomDialog
import com.yarolegovich.lovelydialog.LovelyTextInputDialog


class MainActivity : LocalizationActivity() {

    private var par = true
    private val languages = ArrayList<LanguageItem>()

    lateinit var prefs: SharedPreferences


    private var adsCounter = 1

    var code: String = "es"
    private lateinit var preferences: SharedPreferences

    private val articlesFragment = ArticlesFragment()
    private val todayFragment = CalendarFragment()
    private val prefsFragment = PrefsFragment()

    lateinit var mAdView: AdView

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

        prefs = defaultPrefs(applicationContext)

        MobileAds.initialize(this, "ca-app-pub-1890073619173649~8908748583")
        loadSplashAd()

        //LanguagePrefs
        languages.add(
            LanguageItem(
                R.drawable.ic_english,
                "en",
                "English"
            )
        )
        languages.add(LanguageItem(R.drawable.ic_spain, "es", "Spain"))
        languages.add(
            LanguageItem(
                R.drawable.ic_russia,
                "ru",
                "Русский"
            )
        )

        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY_SECOND)

        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY_THIRD)

        preferences = defaultPrefs(applicationContext)

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

        code = getLanguage(this).language

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

    private fun getCyclePreferences(): List<CycleItem> {
        val cyclePreferences = ArrayList<CycleItem>()

        cyclePreferences.add(
            CycleItem(
                R.drawable.ic_cycle_length,
                "pref_cycle_length",
                getString(R.string.cycle_length),
                ""
            )
        )

        cyclePreferences.add(
            CycleItem(
                R.drawable.ic_menstrual_length,
                "period_length",
                getString(R.string.menstrual_length),
                getString(R.string.cycle_settings_tip_0)
            )
        )

        return cyclePreferences
    }

    private fun getNotificationPreferences(): List<NotificationItem> {
        val notificationPreferences = ArrayList<NotificationItem>()

        notificationPreferences.add(
            NotificationItem(
                getString(R.string.menstrual_two_days),
                "pref_notification_m_two_days",
                R.drawable.ic_notification_m_two_days,
                preferences.getBoolean("pref_notification_m_two_days", false)
            )
        )

        notificationPreferences.add(
            NotificationItem(
                getString(R.string.menstrual_end),
                "pref_notification_m_end",
                R.drawable.ic_notification_m_end,
                preferences.getBoolean("pref_notification_m_end", false)
            )
        )

        notificationPreferences.add(
            NotificationItem(
                getString(R.string.menstrual_start),
                "pref_notification_m_start",
                R.drawable.ic_menstrual_start,
                preferences.getBoolean("pref_notification_m_start", false)
            )
        )

        notificationPreferences.add(
            NotificationItem(
                getString(R.string.ovulation),
                "pref_notification_o",
                R.drawable.ic_ovulation,
                preferences.getBoolean("pref_notification_o", false)
            )
        )

        return notificationPreferences
    }


    fun showCycleDialog() {
        LovelyCustomDialog(this)
            .setView(R.layout.dialog_prefs_cycle)
            .setTopColorRes(R.color.colorPink)
            .setTitle(R.string.cycle_settings)
            .setIcon(R.drawable.ic_cycle_settings)
            .show();

    }

    fun showLanguageDialog() {

        val adapter = DialogLanguageAdapter(this, languages)
        LovelyChoiceDialog(this)
            .setTopColorRes(R.color.colorPink)
            .setTitle(R.string.change_language)
            .setIcon(R.drawable.ic_change_language)
            .setItems(adapter) { _, item ->
                setLanguage(item.code)
            }
            .show()
    }

    fun showNotificationDialog() {

        val adapter = DialogNotificationAdapter(this, getNotificationPreferences())
        LovelyChoiceDialog(this)
            .setTopColorRes(R.color.colorPink)
            .setTitle(R.string.notification_settings)
            .setIcon(R.drawable.ic_notifications)
            .setItemsMultiChoice(adapter) { positions, items ->

                for (position in positions) Log.d("NotificationDialog", "position: $position")

                for (item in items) {
                    preferences.edit().putBoolean(item.code, true).apply()
                    Log.d("NotificationDialog", "put boolean ${item.code} true")
                }
            }
            .show()
    }

    fun showEditTextDialog() {
        LovelyTextInputDialog(this, R.style.EditTextTintTheme)
            .setTopColorRes(R.color.colorPink)
            .setTitle(R.string.whats_your_name)
            .setMessage(R.string.enter_name)
            .setIcon(R.drawable.ic_info_outline_black_24dp)
            .setConfirmButton(android.R.string.ok
            ) { text ->
                    preferences.edit().putString("user_name", text).apply()
                    UserRepository(this).updateUser(prefs.getString("token", "")!!, UserData(text, prefs.getString("color", "#333333")!!))
            }
            .show()
    }

}

