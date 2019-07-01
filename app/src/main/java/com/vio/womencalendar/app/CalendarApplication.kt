package com.vio.womencalendar.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.ads.MobileAds
import com.onesignal.OneSignal
import com.vio.womencalendar.Constants
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import com.vio.womencalendar.Constants.API_KEY


class CalendarApplication: Application() {


    companion object {
        lateinit var prefs: SharedPreferences

        private lateinit var instance: CalendarApplication
        fun getAppContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        instance = this

        super.onCreate()

        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init();

        MobileAds.initialize(this, "ca-app-pub-1890073619173649~8908748583")


        val config = YandexMetricaConfig.newConfigBuilder(API_KEY).build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)

        prefs = this.getSharedPreferences(Constants.PREFERENCES_NAME, 0)
    }

}