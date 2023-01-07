package com.gwtf.flow

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gwtf.flow.Utilites.Constants
import com.gwtf.flow.Utilites.Constants.Business_Selected

class MainActivity : AppCompatActivity() {

    lateinit var bottomBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomBar = findViewById(R.id.bottom_navigation)
        val share = getSharedPreferences(packageName, MODE_PRIVATE)

        loadFragment(HomeFragment())
        bottomBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                }
                R.id.nav_money -> {
                    if (share.getBoolean(Business_Selected + "_WEBSITE", false)) {
                        loadFragment(ShopFragment())
                    } else {
                        startActivity(Intent(this, CreateStoreActivity::class.java))
                    }
                }
                R.id.action_settings-> {
                    loadFragment(SettingsFragment())
                }
            }
            true
        }

    }

    fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }
    }

}