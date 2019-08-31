package com.vio.calendar.view.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vio.calendar.PreferenceHelper.defaultPrefs
import com.vio.calendar.R
import com.vio.calendar.model.prefs.PreferenceItem
import com.vio.calendar.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_prefsa_new.*
import kotlinx.android.synthetic.main.item_profile.userName


class PrefsFragment: Fragment() {

    private lateinit var prefs: SharedPreferences
    val list = ArrayList<PreferenceItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_prefsa_new, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        prefs = defaultPrefs(context.applicationContext)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userName.text = prefs.getString("user_name", "no user name")

        cardUserName.setOnClickListener {
            (activity as MainActivity).showEditTextDialog()
        }

        switch_m_end.isChecked = prefs.getBoolean("notification_m_end", false)
        switch_m_start.isChecked = prefs.getBoolean("notification_m_start", true)
        switch_ovulation.isChecked = prefs.getBoolean("notification_ovulation", true)

        cardCycleAndOvulation.setOnClickListener {
            (activity as MainActivity).showCycleDialog()
        }

        cardLanguage.setOnClickListener {
            (activity as MainActivity).showLanguageDialog()
        }

        switch_m_end.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notification_m_end", isChecked).apply()
        }
        switch_m_start.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notification_m_start", isChecked).apply()
        }
        switch_ovulation.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notification_ovulation", isChecked).apply()
        }
    }

}