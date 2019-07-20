package com.vio.calendar.ui.more

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import androidx.appcompat.app.AppCompatActivity
import com.vio.calendar.R
import kotlinx.android.synthetic.main.activity_more.*


class MoreActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more)

        val moreContent = resources.getStringArray(R.array.more_menu)

        toolbarMore.setNavigationOnClickListener {
            _ -> onBackPressed()
        }

        listViewMore.adapter = (
                ArrayAdapter<String>(
                    this@MoreActivity,
                    android.R.layout.simple_list_item_1, moreContent
                ) as ListAdapter?
        )

        listViewMore.onItemClickListener = AdapterView.OnItemClickListener { _: AdapterView<*>, _: View, position: Int, _: Long ->
            when (position) {
                0 -> showCycleSettingsActivity()
                1 -> showLanguageActivity()
                2 -> showNotificationActivity()
            }
        }
    }

    private fun showLanguageActivity() {
        startActivityForResult(
            Intent(this@MoreActivity, LanguageActivity::class.java), 1
        )
    }

    private fun showCycleSettingsActivity() {
        startActivityForResult(
            Intent(this@MoreActivity, CycleSettingsActivity::class.java), 1
        )
    }

    private fun showNotificationActivity() {
        startActivity(
            Intent(this@MoreActivity, NotificationSettingsActivity::class.java)
        )
    }
}
