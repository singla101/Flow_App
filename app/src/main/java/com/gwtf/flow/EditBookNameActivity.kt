package com.gwtf.flow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.Utilites.getDateTime

class EditBookNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book_name)

        val id = intent.getStringExtra("BookID")

        val txt_name = findViewById<EditText>(R.id.txt_name)
        val btn_submit = findViewById<Button>(R.id.btn_submit)
        val db = SqlDatabase(this)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        btn_submit.setOnClickListener {
            if (txt_name.text.isEmpty()) {
                Toast.makeText(this, "Enter Valid Book Name", Toast.LENGTH_SHORT).show()
            } else {
                db.updateBookName(id, txt_name.text.toString(), getDateTime.getMilies())
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}