package com.vio.calendar.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate
import com.onesignal.OneSignal
import com.vio.calendar.Constants
import com.vio.calendar.Constants.API_KEY
import com.vio.calendar.PreferenceHelper.defaultPrefs
import com.vio.calendar.db.PeriodicalDatabase
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig


class CalendarApplication: Application() {

    var localizationDelegate = LocalizationApplicationDelegate(this)

    lateinit var dbMain: PeriodicalDatabase
    lateinit var prefs: SharedPreferences


    companion object {
        lateinit var preferences: SharedPreferences

        private lateinit var instance: CalendarApplication
        fun getAppContext(): Context = instance.applicationContext

    }

    override fun onCreate() {
        instance = this

        super.onCreate()
        initDatabase()


        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init();

        val config = YandexMetricaConfig.newConfigBuilder(API_KEY).build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)

        prefs = defaultPrefs(this)

        preferences = this.getSharedPreferences(Constants.PREFERENCES_NAME, 0)
    }

    fun initDatabase() {
        dbMain = PeriodicalDatabase(applicationContext)
        dbMain.restorePreferences()
        dbMain.loadCalculatedData()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localizationDelegate.onConfigurationChanged(this)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

}