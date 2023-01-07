package com.gwtf.flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.api.Distribution.BucketOptions.Linear

class CashSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_settings)
        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val btnLight = findViewById<LinearLayout>(R.id.btnLight)
        val partyStatus = findViewById<TextView>(R.id.partyStatus)
        val hindiBtn = findViewById<LinearLayout>(R.id.hindiBtn)
        val categoryStatus = findViewById<TextView>(R.id.categoryStatus)
        val modeBtn = findViewById<LinearLayout>(R.id.modeBtn)
        val modeStatus = findViewById<TextView>(R.id.modeStatus)

        if (sharedPreferences.getBoolean("isParty", true))
            partyStatus.text = "ON"
        else
            partyStatus.text = "OFF"

        if (sharedPreferences.getBoolean("isCategory", true))
            categoryStatus.text = "ON"
        else
            categoryStatus.text = "OFF"


        if (sharedPreferences.getBoolean("isMode", true))
            modeStatus.text = "ON"
        else
            modeStatus.text = "OFF"


        btnLight.setOnClickListener {
            editor.putBoolean("isParty", !sharedPreferences.getBoolean("isParty", true))
            editor.apply()
            if (sharedPreferences.getBoolean("isParty", true))
                partyStatus.text = "ON"
            else
                partyStatus.text = "OFF"
        }

        hindiBtn.setOnClickListener {
            editor.putBoolean("isCategory", !sharedPreferences.getBoolean("isCategory", true))
            editor.apply()
            if (sharedPreferences.getBoolean("isCategory", true))
                categoryStatus.text = "ON"
            else
                categoryStatus.text = "OFF"
        }

        modeBtn.setOnClickListener {
            editor.putBoolean("isMode", !sharedPreferences.getBoolean("isMode", true))
            editor.apply()
            if (sharedPreferences.getBoolean("isMode", true))
                modeStatus.text = "ON"
            else
                modeStatus.text = "OFF"
        }

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }

    }
}