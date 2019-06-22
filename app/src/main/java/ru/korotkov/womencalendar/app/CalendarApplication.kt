package ru.korotkov.womencalendar.app

import android.app.Application
import android.content.SharedPreferences
import ru.korotkov.womencalendar.Constants
import ru.korotkov.womencalendar.model.date.Date
import ru.korotkov.womencalendar.model.date.DateRange
import java.util.*


class CalendarApplication: Application() {

    companion object {
        lateinit var prefs: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        prefs = this.getSharedPreferences(Constants.PREFERENCES_NAME, 0)
    }

}