package com.vio.calendar.ui.main

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.akexorcist.localizationactivity.core.LanguageSetting.getLanguage
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.google.android.gms.ads.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vio.calendar.PreferenceHelper.defaultPrefs
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.data.user.UserRepository
import com.vio.calendar.data.user.model.UserData
import com.vio.calendar.model.dialog.LanguageItem
import com.vio.calendar.setTransparentStatusBar
import com.vio.calendar.ui.articles.ArticlesFragment
import com.vio.calendar.ui.calendar.CalendarFragment
import com.vio.calendar.view.adapters.DialogLanguageAdapter
import com.vio.calendar.view.fragments.PrefsFragment
import com.yarolegovich.lovelydialog.LovelyChoiceDialog
import com.yarolegovich.lovelydialog.LovelyCustomDialog
import com.yarolegovich.lovelydialog.LovelyTextInputDialog


class MainActivity : LocalizationActivity() {

    private val languages = ArrayList<LanguageItem>()
    private var databaseHelper = CalendarApplication.getDatabaseHelper()

    lateinit var prefs: SharedPreferences


    private var adsCounter = 1

    var code: String = "es"
    private lateinit var preferences: SharedPreferences

    private val articlesFragment = ArticlesFragment()
    private val todayFragment = CalendarFragment()
    private val prefsFragment = PrefsFragment()

    var counter = 5

    private lateinit var mInterstitialAdScreens: InterstitialAd


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        hideBottomPanel()

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
        if (adsCounter % counter == 1) {
            Log.d(
                "MainActivity", "trying to show screens ad, " +
                        "ad loaded: ${mInterstitialAdScreens.isLoaded}, " +
                        "ad loading: ${mInterstitialAdScreens.isLoading}"
            )
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = ContextCompat.getColor(applicationContext, R.color.transparent)
        }

        databaseHelper.setOption("launch", 1)


        hideBottomPanel()

        prefs = defaultPrefs(applicationContext)
        //initAds()

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


        preferences = defaultPrefs(applicationContext)




        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


        code = getLanguage(this).language

        switchToFragment(todayFragment)

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


    fun showEditTextDialog() {
        LovelyTextInputDialog(this, R.style.EditTextTintTheme)
            .setTopColorRes(R.color.colorPink)
            .setTitle(R.string.whats_your_name)
            .setMessage(R.string.enter_name)
            .setIcon(R.drawable.ic_info_outline_black_24dp)
            .setConfirmButton(
                android.R.string.ok
            ) { text ->
                preferences.edit().putString("user_name", text).apply()
                UserRepository(this).updateUser(
                    prefs.getString("token", "")!!,
                    UserData(text, prefs.getString("color", "#333333")!!)
                )
            }
            .show()
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


    private fun initAds() {

        counter = prefs.getInt("counter", 5)
        Log.d("MainActivity", "ads counter: $counter")

        // add AdMob
        val adView = AdView(this)
        //adView.adUnitId = "ca-app-pub-3940256099942544/6300978111" -- test id
        adView.adUnitId = prefs.getString("topId", "ca-app-pub-1890073619173649/4909793826")
        adView.adListener = object : AdListener() {

            override fun onAdLoaded() {
                Log.d("MainActivity", "ad top: loaded")
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                Log.d("MainActivity", "ad top: failed to load with error $errorCode")
            }

            override fun onAdOpened() {
                Log.d("MainActivity", "ad top: opened")
            }
        }
        adView.adSize = AdSize.BANNER

        val adLayoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        adLayoutParams.topMargin = 20

        adView.layoutParams = adLayoutParams

        val layout = LinearLayout(this)
        layout.addView(adView)
        layout.gravity = Gravity.TOP

        val layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        addContentView(layout, layoutParams)
        adView.loadAd(AdRequest.Builder().addTestDevice("22A3ECE5BE9E7A2422F93F23C9F905C8").build())

        //screens ad
        mInterstitialAdScreens = InterstitialAd(this)
        //mInterstitialAdScreens.adUnitId = "ca-app-pub-3940256099942544/1033173712" -- test Id
        mInterstitialAdScreens.adUnitId = prefs.getString("screensId", "ca-app-pub-1890073619173649/8078882648")
        Log.d("MainActivity", "ad screens id: ${mInterstitialAdScreens.adUnitId}")
        mInterstitialAdScreens.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log.d("MainActivity", "ad screens: loaded")
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                Log.d("MainActivity", "ad screens: failed to load with error $errorCode")
            }

            override fun onAdOpened() {
                Log.d("MainActivity", "ad screens: opened")
            }

            override fun onAdClosed() {
                mInterstitialAdScreens.loadAd(AdRequest.Builder().addTestDevice("22A3ECE5BE9E7A2422F93F23C9F905C8").build())
            }
        }
        mInterstitialAdScreens.loadAd(AdRequest.Builder().addTestDevice("22A3ECE5BE9E7A2422F93F23C9F905C8").build())


        val mAdSplash = InterstitialAd(this)
        mAdSplash.adUnitId = prefs.getString("splashId", "ca-app-pub-1890073619173649/2395311128")
        //mAdSplash.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mAdSplash.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log.d("MainActivity", "ad splash: loaded")
                mAdSplash.show()
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                Log.d("MainActivity", "ad splash: failed to load with error $errorCode")
            }

            override fun onAdOpened() {
                Log.d("MainActivity", "ad splash: opened")
            }

            override fun onAdClosed() {
                mInterstitialAdScreens.loadAd(AdRequest.Builder().addTestDevice("22A3ECE5BE9E7A2422F93F23C9F905C8").build())
            }
        }
        mAdSplash.loadAd(AdRequest.Builder().addTestDevice("22A3ECE5BE9E7A2422F93F23C9F905C8").build())
    }
}

