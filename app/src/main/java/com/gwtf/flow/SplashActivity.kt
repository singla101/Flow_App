package com.gwtf.flow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.Utilites.Constants
import com.gwtf.flow.Utilites.LocaleHelper

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        val sharedPref = getSharedPreferences(packageName, MODE_PRIVATE)

        if (sharedPref.getString("mode", "").equals("dark"))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else if (sharedPref.getString("mode", "").equals("light"))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        if (sharedPref.getString("AppLang", "").equals("hi")) {
            LocaleHelper.setLocale(this, "hi")
            getResources()
        } else if (sharedPref.getString("AppLang", "").equals("pa")) {
            LocaleHelper.setLocale(this, "pa")
            getResources()
        } else {
            LocaleHelper.setLocale(this, "en")
            getResources()
        }


        val db = SqlDatabase(this)

        val mAuth = FirebaseAuth.getInstance()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            if (mAuth.currentUser != null) {
                if (db.business.size <= 0) {
                    val intent = Intent(this, CreateBusinessActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Constants.Business_Selected =
                        sharedPref.getString("BusinessSelected", null).toString()
                    Constants.Business_Name = sharedPref.getString("BusinessName", null).toString()
                    Constants.Business_Image =
                        sharedPref.getString("BusinessImage", null).toString()
                    Constants.UserName = sharedPref.getString("UserName", null).toString()
                    Constants.PhoneNumber = sharedPref.getString("PhoneNumber", null).toString()
                    Constants.Email = sharedPref.getString("Email", null).toString()
                    Constants.Address = sharedPref.getString("Address", null).toString()

                    startActivity(
                        Intent(
                            this, MainActivity
                            ::class.java
                        )
                    )
                    finish()
                }
            } else {
                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000)
    }
}