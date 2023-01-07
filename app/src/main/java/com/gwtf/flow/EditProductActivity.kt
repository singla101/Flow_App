package com.gwtf.flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.gwtf.flow.R
import com.gwtf.flow.adapter.ImageAdapter
import com.gwtf.flow.model.ImageModel

class EditProductActivity : AppCompatActivity() {


    var list = ArrayList<ImageModel>()
    var _id: String = ""
    var _title: String = ""
    var _businessID: String = ""
    var _category: String = ""
    var _price: String = ""
    var _description: String = ""
    var _inStock: Boolean = true

    lateinit var adapter: ImageAdapter
    lateinit var recyclerView: RecyclerView

    lateinit var txt_title: EditText
    lateinit var txt_price: EditText
    lateinit var txt_category: EditText
    lateinit var txt_dicription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        recyclerView = findViewById(R.id.images)
        txt_title = findViewById(R.id.txt_title)
        txt_price = findViewById(R.id.txt_price)
        txt_category = findViewById(R.id.txt_category)
        txt_dicription = findViewById(R.id.txt_dicription)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager

        val db = FirebaseFirestore.getInstance()
        db.collection("PRODUCTS").document(intent.getStringExtra("Id").toString()).get().addOnSuccessListener { task ->
            _id = task.id

            txt_title.setText(task.get("TITLE").toString())
            txt_price.setText(task.get("PRICE").toString())
            txt_category.setText(task.get("CATEGORY").toString())
            txt_dicription.setText(task.get("DESCRIPTION").toString())

            _title = task.get("TITLE").toString()
            _businessID = task.get("BUSINESSID").toString()
            _category = task.get("CATEGORY").toString()
            _price = task.get("PRICE").toString()
            _description = task.get("DESCRIPTION").toString()
            _inStock = task.getBoolean("InStock") == true
            list.add(ImageModel(task.get("Image1").toString()))
            list.add(ImageModel(task.get("Image2").toString()))
            list.add(ImageModel(task.get("Image3").toString()))
            list.add(ImageModel(task.get("Image4").toString()))
            
            adapter = ImageAdapter(list)
            recyclerView.adapter = adapter
        }

    }
}