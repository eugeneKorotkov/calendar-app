package com.vio.calendar.ui.more

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import kotlinx.android.synthetic.main.activity_language.*

class LanguageActivity : AppCompatActivity() {

    private val listLanguages = ArrayList<LanguageItem>()
    private lateinit var adapter: LanguageRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        toolbarLanguage.setNavigationOnClickListener {
                _ -> onBackPressed()
        }

        listLanguages.add(LanguageItem(R.drawable.ic_like_false, "ru", "Русский"))
        listLanguages.add(LanguageItem(R.drawable.ic_like_false, "en", "English"))
        listLanguages.add(LanguageItem(R.drawable.ic_like_false, "es", "Spain"))

        adapter = LanguageRecyclerAdapter(listLanguages) {
            CalendarApplication.prefs.edit().putString("language", it.code).apply()
            Toast.makeText(this, getString(R.string.info_restart_application), Toast.LENGTH_LONG).show()
        }

        recyclerViewLanguage.adapter = adapter


    }
}
