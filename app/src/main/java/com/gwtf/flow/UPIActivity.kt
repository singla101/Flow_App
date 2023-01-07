package com.gwtf.flow

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.cardview.widget.CardView
import com.gwtf.flow.Utilites.Constants

class UPIActivity : AppCompatActivity() {


    val CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";
    private var mCustomTabsServiceConnection: CustomTabsServiceConnection? = null
    private var mClient: CustomTabsClient? = null
    private var mCustomTabsSession: CustomTabsSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upiactivity)

        val upicard = findViewById<CardView>(R.id.upicard)
        upicard.visibility = View.GONE

        val txt_amount = findViewById<TextView>(R.id.txt_amount)
        val saveBtn = findViewById<Button>(R.id.saveBtn)
        val shareBtn = findViewById<Button>(R.id.shareBtn)
        val editBtn = findViewById<Button>(R.id.editBtn)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        val edit = sharedPreferences.edit()


        if (sharedPreferences.getString("UPIID", "").equals("") || sharedPreferences.getString("UPIID", "").equals(null))
            upicard.visibility = View.GONE
        else {
            upicard.visibility = View.VISIBLE
            txt_amount.visibility = View.GONE
            saveBtn.visibility = View.GONE
        }

        saveBtn.setOnClickListener {
            edit.putString("UPIID", txt_amount.text.toString())
            edit.apply()
            upicard.visibility = View.VISIBLE
            txt_amount.visibility = View.GONE
            saveBtn.visibility = View.GONE
        }

        editBtn.setOnClickListener {
            txt_amount.text = sharedPreferences.getString("UPIID", "")
            upicard.visibility = View.GONE
            txt_amount.visibility = View.VISIBLE
            saveBtn.visibility = View.VISIBLE
        }

        shareBtn.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hi, \n\nYou can pay me using this UPI link: upi://pay?pa=" + sharedPreferences.getString("UPIID", "") + " \n\nRegards,\n" + Constants.Business_Name)
            shareIntent.type = "text/plain"
            startActivity(Intent.createChooser(shareIntent,"send to"))
        }


    }
}