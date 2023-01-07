package com.gwtf.flow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.gwtf.flow.Database.SqlDatabase

class DuplicateBookActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_duplicate_book)

        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")

        val db = SqlDatabase(this)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        val txt_name = findViewById<EditText>(R.id.txt_name)
        val txt_bookname = findViewById<TextView>(R.id.txt_bookname)
        val btn_submit = findViewById<Button>(R.id.btn_submit)
        txt_bookname.text = "Please type "+ name +" to confirm"

        btn_submit.setOnClickListener {
            if (txt_name.text.toString().equals(name)) {
                db.deleteBook(id, name)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                txt_name.error = "Enter Correct Business Book Name"
            }
        }
    }
}