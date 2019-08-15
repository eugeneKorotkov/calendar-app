package com.vio.calendar.view.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.vio.calendar.PreferenceHelper.defaultPrefs
import com.vio.calendar.R
import com.vio.calendar.model.prefs.PreferenceItem
import com.vio.calendar.ui.main.MainActivity
import com.vio.calendar.view.adapters.PrefsRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_prefs.*


class PrefsFragment: Fragment() {

    private lateinit var prefs: SharedPreferences
    val list = ArrayList<PreferenceItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_prefs, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        prefs = defaultPrefs(context)
        list.clear()
        list.add(
            PreferenceItem(
                getString(R.string.cycle_settings),
                "",
                "",
                R.drawable.ic_cycle_settings,
                0
            )
        )
        list.add(
            PreferenceItem(
                getString(R.string.notification_settings),
                "",
                "",
                R.drawable.ic_notifications,
                1
            )
        )
        list.add(
            PreferenceItem(
                getString(R.string.change_language),
                "",
                "",
                R.drawable.ic_change_language,
                2
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefsRecycler.layoutManager = LinearLayoutManager(context)
        prefsRecycler.adapter = PrefsRecyclerAdapter(
            list,
            prefs,
            {(activity as MainActivity).showLanguageDialog()},
            {(activity as MainActivity).showNotificationDialog()}
        )

    }

}