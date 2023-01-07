package com.gwtf.flow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.Utilites.Constants
import com.gwtf.flow.Utilites.Constants.UserName
import com.gwtf.flow.Utilites.IDGenrator
import com.gwtf.flow.Utilites.getDateTime

class AddNewBusinessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_business)

        val txt_business: EditText = findViewById(R.id.txt_business_name)
        val btn_submit: Button = findViewById(R.id.btn_submit)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        txt_business.requestFocus()
        txt_business.doOnTextChanged { text, start, before, count ->
            if (txt_business.text.length >= 0)
                btn_submit.isEnabled = true
        }

        btn_submit.isEnabled = false

        var Id = IDGenrator.getId("Business")
        val db = SqlDatabase(this)
        val sharedPref = getSharedPreferences(packageName, MODE_PRIVATE)
        val editor = sharedPref.edit()
        btn_submit.setOnClickListener {
            if (txt_business.text.length >= 0) {
                db.addBusiness(
                    Id,
                    UserName,
                    txt_business.text.toString(),
                    "",
                    "",
                    getDateTime.getDate(),
                    getDateTime.getTime()
                )
                editor.apply {
                    putString("BusinessSelected", Id)
                    putString("BusinessName", txt_business.text.toString())
                    putString("BusinessImage", "")
                    apply()
                }
                startActivity(Intent(this, SelectBusinessCategoryActivity::class.java)
                    .putExtra("BusinessId", Id)
                )
                finish()
            } else {
                txt_business.error = "Enter valid business name"
            }
        }
    }
}