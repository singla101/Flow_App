package com.gwtf.flow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.api.Distribution.BucketOptions.Linear
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.adapter.BusinessCategoryAdapter
import com.gwtf.flow.model.CategoriesModel

class SelectBusinessCategoryActivity : AppCompatActivity() {

    companion object {
        lateinit var listBusiness: RecyclerView
        var isSelected = false
        var type: String = ""
        lateinit var btn_submit: Button
        lateinit var database: SqlDatabase
        var list = ArrayList<CategoriesModel>()

        fun getData() {
            list.clear()
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/3227/3227831.png", "Agriculture"))
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/2823/2823550.png", "Construction"))
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/3976/3976625.png", "Education"))
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/1261/1261143.png", "Electronics"))
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/1907/1907675.png", "Financial\nServices"))
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/737/737967.png", "Food\nRestaurant"))
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/3275/3275391.png", "Clothes\nFashion"))
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/3868/3868395.png", "Hardware"))
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/3674/3674421.png", "Jewellery"))
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/2382/2382533.png", "Healthcare &\n Fitness"))
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/1261/1261163.png", "Kirana\n/Grocery"))
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/3322/3322068.png", "Transport"))
            list.add(CategoriesModel("https://cdn-icons-png.flaticon.com/128/7245/7245102.png", "Other"))
            val adadper = BusinessCategoryAdapter(list)
            listBusiness.adapter = adadper
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_business_category)
        database = SqlDatabase(this)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        val BusinessId = intent.getStringExtra("BusinessId")

        val btnSkip = findViewById<TextView>(R.id.btnSkip)
        btn_submit = findViewById(R.id.btn_submit)

        btnSkip.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        listBusiness = findViewById(R.id.listBusiness)
        val lmanager = GridLayoutManager(this, 2)
        lmanager.orientation = LinearLayoutManager.VERTICAL
        listBusiness.layoutManager = lmanager
        getData()

        btn_submit.setOnClickListener {
            if (isSelected) {
                database.updateBusinessCategoryTYpe(BusinessId, type, "")
                startActivity(Intent(this, SelectBusinessTypeActivity::class.java)
                    .putExtra("Id", BusinessId)
                    .putExtra("category", type)
                )
            }
        }

    }
}