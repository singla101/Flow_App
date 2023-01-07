package com.gwtf.flow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.Utilites.Constants.*
import com.gwtf.flow.Utilites.IDGenrator
import com.gwtf.flow.Utilites.getDateTime

class CreateBusinessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_business)
        val mStore = FirebaseFirestore.getInstance()
        val mAuth = FirebaseAuth.getInstance()
        val txt_name: EditText = findViewById(R.id.txt_name)
        val txt_business: EditText = findViewById(R.id.txt_business_name)
        val chk_not_business: CheckBox = findViewById(R.id.chk_not_business)
        val btn_submit: Button = findViewById(R.id.btn_submit)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        chk_not_business.setOnCheckedChangeListener { compoundButton, b ->
            txt_business.isEnabled = !b
        }
        btn_submit.isEnabled = true

        var Id = IDGenrator.getId("Business")
        val db = SqlDatabase(this)
        val sharedPref = getSharedPreferences(packageName, MODE_PRIVATE)
        val editor = sharedPref.edit()
        btn_submit.setOnClickListener {
            if (chk_not_business.isChecked) {
                db.addBusiness(Id, txt_name.text.toString(),"" + txt_name.text.toString() + "'s Business", "", "", getDateTime.getDate(), getDateTime.getTime())
                editor.apply {
                    putString("BusinessSelected", Id)
                    putString("BusinessName", txt_name.text.toString() + "'s Business")
                    putString("BusinessImage", "")
                    apply()
                }
            } else {
                db.addBusiness(Id, txt_name.text.toString() , txt_business.text.toString(), "", "", getDateTime.getDate(), getDateTime.getTime())
                editor.apply {
                    putString("BusinessSelected", Id)
                    putString("BusinessName", txt_business.text.toString())
                    putString("BusinessImage", "")
                    apply()
                }
            }

            val userData = hashMapOf(
                "USER_NAME" to txt_name.text.toString(),
                "USER_EMAIL" to Email,
                "USER_MOBILE" to PhoneNumber,
                "UID" to mAuth.uid.toString()
            )
            mStore.collection("USER").document(mAuth.uid.toString()).set(userData)

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}