package com.gwtf.flow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.adapter.BusinessCategoryAdapter
import com.gwtf.flow.adapter.BusinessTypeAdapter
import com.gwtf.flow.model.CategoriesModel

class SelectBusinessTypeActivity : AppCompatActivity() {

    companion object {
        lateinit var listBusiness: RecyclerView
        var isSelected = false
        var type: String = ""
        lateinit var btn_submit: Button
        lateinit var database: SqlDatabase
        var list = ArrayList<CategoriesModel>()

        fun getData() {
            list.clear()
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/3733/3733121.png", "Retailer"))
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/3505/3505356.png", "Distributor"))
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/2299/2299203.png", "Manufacturer"))
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/3715/3715149.png", "Service Provider"))
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/6417/6417455.png", "Trader"))
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/7245/7245102.png", "Other"))
            val adadper = BusinessTypeAdapter(list)
            listBusiness.adapter = adadper
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_business_type)
        database = SqlDatabase(this)

        val BusinessId = intent.getStringExtra("Id")
        val category = intent.getStringExtra("category")

        val btnSkip = findViewById<TextView>(R.id.btnSkip)
        btn_submit = findViewById(R.id.btn_submit)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        btnSkip.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        listBusiness = findViewById(R.id.listBusiness)
        val lmanager = GridLayoutManager(this, 1)
        lmanager.orientation = LinearLayoutManager.VERTICAL
        listBusiness.layoutManager = lmanager
        getData()

        btn_submit.setOnClickListener {
            if (isSelected) {
                database.updateBusinessCategoryTYpe(BusinessId,
                    category , type)
                startActivity(
                    Intent(this, MainActivity::class.java)
                )
            }
        }
    }
}