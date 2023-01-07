package com.gwtf.flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import com.gwtf.flow.R

class ThemeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)
        val hindiBtn = findViewById<LinearLayout>(R.id.hindiBtn)
        val btnLight = findViewById<LinearLayout>(R.id.btnLight)
        val autoBtn = findViewById<LinearLayout>(R.id.autoBtn)

        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        val edit = sharedPreferences.edit()

        hindiBtn.setOnClickListener {
            edit.putString("mode", "dark")
            edit.apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        btnLight.setOnClickListener {
            edit.putString("mode", "light")
            edit.apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        autoBtn.setOnClickListener {
            edit.putString("mode", "auto")
            edit.apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener { onBackPressed() }
    }
}