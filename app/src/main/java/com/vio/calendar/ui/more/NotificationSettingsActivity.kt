package com.vio.calendar.ui.more

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vio.calendar.R
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationSettingsActivity : AppCompatActivity() {

    private val settingsList = ArrayList<NotificationItem>()
    private lateinit var adapter: NotificationRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        toolbarNotificationSettings.setNavigationOnClickListener {
                _ -> onBackPressed()
        }

        settingsList.add(NotificationItem(getString(R.string.notification_question_1), false))
        settingsList.add(NotificationItem(getString(R.string.notification_question_2), false))
        settingsList.add(NotificationItem(getString(R.string.notification_question_3), false))

        adapter = NotificationRecyclerAdapter(settingsList) {}

    }
}
