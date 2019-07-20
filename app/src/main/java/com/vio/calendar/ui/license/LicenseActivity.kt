package com.vio.calendar.ui.license

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.vio.calendar.Constants
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.ui.steps.StepsActivity
import kotlinx.android.synthetic.main.activity_license.*

class LicenseActivity : AppCompatActivity() {

    private lateinit var adapter: LicenseRecyclerAdapter
    private lateinit var licenseContent: ArrayList<LicenseItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_license)

        for (itemString in resources.getStringArray(R.array.more_menu)) {
            licenseContent.add(LicenseItem(false, itemString))
        }

        adapter = LicenseRecyclerAdapter(licenseContent) {
            it.isChecked = it.isChecked.not()
            if (adapter.isAllChecked()) {
                button_next.isClickable = true
                button_next.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                button_next.setBackgroundResource(R.drawable.rounded_button)
                button_accept_all.visibility = View.GONE
            } else
                button_next.isClickable = false
                button_next.setBackgroundResource(android.R.color.transparent)
        }
        recyclerViewLicense.adapter = adapter

        button_accept_all.setOnClickListener {
            adapter.checkAll()
        }

        button_next.setOnClickListener {
            CalendarApplication.prefs.edit().putBoolean(Constants.LICENSE, true).apply()
            val intent = Intent(this, StepsActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


}