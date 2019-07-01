package com.vio.womencalendar.model.preferences

import android.preference.PreferenceManager
import com.google.gson.Gson
import com.vio.womencalendar.app.CalendarApplication
import com.vio.womencalendar.model.user.UserInfo

object UserInfoPreferences {

    private const val KEY_USER_INFO = "KEY_AUTH_TOKEN"
    private val gson: Gson = Gson()

    private fun sharedPrefs() = PreferenceManager.getDefaultSharedPreferences(CalendarApplication.getAppContext())

    fun updateUserInfo(userInfo: UserInfo) {
        val editor = sharedPrefs().edit()
        val jsonString = gson.toJson(userInfo)
        editor.putString(KEY_USER_INFO, jsonString).apply()
    }

    fun getUserInfo(): UserInfo {
        val jsonString = sharedPrefs().getString(KEY_USER_INFO, "")
        if (jsonString == "") return UserInfo()
        return gson.fromJson(jsonString, UserInfo::class.java)
    }

}