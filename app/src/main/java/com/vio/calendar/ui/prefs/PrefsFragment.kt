package com.vio.calendar.ui.prefs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.vio.calendar.R
import com.vio.calendar.model.prefs.PreferenceItem
import kotlinx.android.synthetic.main.fragment_prefs.*

class PrefsFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
        return inflater.inflate(R.layout.fragment_prefs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefsRecycler.layoutManager = LinearLayoutManager(context)
        val list = ArrayList<PreferenceItem>()
        list.add(
            PreferenceItem(
                "key",
                "title",
                "summary",
                R.drawable.ic_change_language,
                22
            )
        )
        list.add(
            PreferenceItem(
                "key",
                "title2",
                "summary2",
                R.drawable.ic_notification_m_end,
                22
            )
        )
        list.add(
            PreferenceItem(
                "key",
                "title3",
                "summary3",
                R.drawable.ic_change_language,
                22
            )
        )
        list.add(
            PreferenceItem(
                "key",
                "title3",
                "summary3",
                R.drawable.ic_change_language,
                22
            )
        )
        list.add(
            PreferenceItem(
                "key",
                "title5",
                "summary4",
                R.drawable.ic_change_language,
                22
            )
        )
        list.add(
            PreferenceItem(
                "key",
                "title4",
                "summary5",
                R.drawable.ic_change_language,
                22
            )
        )

        prefsRecycler.adapter = PrefsRecyclerAdapter(list)


    }

}