package com.gwtf.flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.gwtf.flow.R
import com.gwtf.flow.adapter.ProductAdapter
import com.gwtf.flow.model.ProductModel

class InvantoryActivity : AppCompatActivity() {

    lateinit var listProduct: RecyclerView
    lateinit var adapter: ProductAdapter
    val list = ArrayList<ProductModel>()
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invantory)

        listProduct = findViewById(R.id.listProduct)
        val layManager = GridLayoutManager(this, 1)
        layManager.orientation = LinearLayoutManager.VERTICAL
        listProduct.layoutManager = layManager

        db = FirebaseFirestore.getInstance()

        getData()

    }

    fun getData () {
        db.collection("PRODUCTS").get().addOnSuccessListener { result ->
            for (doc in result) {
                list.add(
                    ProductModel(doc.id, doc.get("TITLE").toString(), doc.get("Image1").toString(),
                doc.get("PRICE").toString(), doc.get("DESCRIPTION").toString(), doc.get("BUSINESSID").toString(),
                doc.get("CATEGORY").toString(), doc.getBoolean("InStock") == true))
            }
            adapter = ProductAdapter(list)
            listProduct.adapter = adapter
        }
    }
}