package com.vio.calendar.ui.license

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.vio.calendar.Constants
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.ui.steps.StepsActivity
import kotlinx.android.synthetic.main.activity_license.*



class LicenseActivity : LocalizationActivity() {

    private lateinit var adapter: LicenseRecyclerAdapter
    private val licenseContent = ArrayList<LicenseItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_license)

        for (itemString in resources.getStringArray(R.array.license_menu)) {
            licenseContent.add(LicenseItem(false, itemString))
        }

        adapter = LicenseRecyclerAdapter(licenseContent) {
            it.isChecked = true
            if (adapter.isAllChecked()) {
                licenseAgree()
            }
        }

        recyclerViewLicense.layoutManager = LinearLayoutManager(this)
        recyclerViewLicense.adapter = adapter

        button_accept_all.setOnClickListener {
            adapter.checkAll()
            licenseAgree()
        }

        button_next.setOnClickListener {
            CalendarApplication.prefs.edit().putBoolean(Constants.LICENSE, true).apply()
            val intent = Intent(this, StepsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun licenseAgree() {
        button_next.isClickable = true
        button_next.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        button_next.setBackgroundResource(R.drawable.button_green)
        button_accept_all.visibility = View.GONE
    }
}