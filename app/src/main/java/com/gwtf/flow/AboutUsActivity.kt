package com.gwtf.flow

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class AboutUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        val gitgur = findViewById<ImageView>(R.id.gitgur)
        gitgur.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Gurjotharika")))
        }
        val instagur = findViewById<ImageView>(R.id.instagur)
        instagur.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/gurjotsinghharika/")))
        }
        val gitsummit = findViewById<ImageView>(R.id.gitsummit)
        gitsummit.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/singla101")))
        }
        val instasummit = findViewById<ImageView>(R.id.instasummit)
        instasummit.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/itz_sumit.singla/")))
        }

        val gittanis = findViewById<ImageView>(R.id.gittanis)
        gittanis.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Tanisha345")))
        }

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        val contact = findViewById<Button>(R.id.contact)
        contact.setOnClickListener {
            val subject = "Support: Flow"
            val intent = Intent(Intent.ACTION_SEND)
            val addressees = arrayOf("gurjotsinghharika@gmail.com")
            intent.putExtra(Intent.EXTRA_EMAIL, addressees)
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.setType("message/rfc822")
            startActivity(Intent.createChooser(intent, "Send Email using:"));
        }



    }
}