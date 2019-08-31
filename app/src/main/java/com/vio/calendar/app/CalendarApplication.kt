package com.vio.calendar.app

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate
import com.google.android.gms.ads.MobileAds
import com.onesignal.OneSignal
import com.vio.calendar.Constants.API_KEY
import com.vio.calendar.PreferenceHelper.defaultPrefs
import com.vio.calendar.data.user.UserRepository
import com.vio.calendar.db.DatabaseHelper
import com.vio.calendar.receivers.AlarmBroadcastReceiver
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import java.util.*


class CalendarApplication: Application() {

    var localizationDelegate = LocalizationApplicationDelegate(this)
    lateinit var prefs: SharedPreferences

    private lateinit var databaseHelper: DatabaseHelper

    companion object {

        private lateinit var instance: CalendarApplication
        fun getAppContext(): Context = instance.applicationContext

        fun getDatabaseHelper(): DatabaseHelper = instance.databaseHelper

    }

    override fun onCreate() {
        instance = this

        super.onCreate()
        databaseHelper = DatabaseHelper(this)


        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init();

        val config = YandexMetricaConfig.newConfigBuilder(API_KEY).build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)

        prefs = defaultPrefs(this)
        initAdMobSettings(this)
        startAlarmBroadcastReceiver(this)
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

    private fun initAdMobSettings(context: Context) {
        UserRepository(context).getSettings()
        MobileAds.initialize(this, "ca-app-pub-1890073619173649~8908748583")
    }

    private fun startAlarmBroadcastReceiver(context: Context) {
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 30)
        calendar.set(Calendar.SECOND, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

}