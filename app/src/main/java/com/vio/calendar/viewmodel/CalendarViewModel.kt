package com.vio.calendar.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.vio.calendar.db.PeriodicalDatabase

class CalendarViewModel(application: Application): AndroidViewModel(application)  {

    private var dbMain: PeriodicalDatabase = PeriodicalDatabase(application)

    init {
        dbMain.restorePreferences()
        dbMain.loadCalculatedData()
    }

}