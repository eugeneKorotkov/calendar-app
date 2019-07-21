package com.vio.calendar.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.onesignal.OneSignal
import com.vio.calendar.Constants
import com.vio.calendar.Constants.API_KEY
import com.vio.calendar.db.PeriodicalDatabase
import com.vio.calendar.utils.LocaleUtils
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import java.util.*


class CalendarApplication: Application() {

    lateinit var dbMain: PeriodicalDatabase

    companion object {
        lateinit var prefs: SharedPreferences

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

        prefs = this.getSharedPreferences(Constants.PREFERENCES_NAME, 0)

        //set Locale
        LocaleUtils.setLocale(Locale(prefs.getString("language", Locale.getDefault().toString())))
        LocaleUtils.updateConfig(this, baseContext.resources.configuration);
    }

    fun initDatabase() {
        dbMain = PeriodicalDatabase(applicationContext)
        dbMain.restorePreferences()
        dbMain.loadCalculatedData()
    }

    fun get() {
        val integerArray = ArrayList<Int>()

        val reader = Scanner(System.`in`)
        var integer: Int = reader.nextInt()
        for (i in 0..integer) {
            integerArray.add(reader.nextInt())
        }
        val integerSet = integerArray.toSet()

        var sum = 0
        for (i in integerSet) {
            sum =+ i
        }
        print(sum)
    }

}