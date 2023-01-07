package com.gwtf.flow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.gwtf.flow.R
import com.gwtf.flow.adapter.ImageAdapter
import com.gwtf.flow.model.ImageModel

class ProductViewActivity : AppCompatActivity() {

    var list = ArrayList<ImageModel>()
    var _id: String = ""
    var _title: String = ""
    var _businessID: String = ""
    var _category: String = ""
    var _price: String = ""
    var _description: String = ""
    var _inStock: Boolean = true

    companion object {
        lateinit var mainImage: ImageView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_view)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        val titleTxt = findViewById<TextView>(R.id.titleTxt)
        val priceTxt = findViewById<TextView>(R.id.priceTxt)
        val dicriptionTxt = findViewById<TextView>(R.id.dicriptionTxt)
        val qty = findViewById<Switch>(R.id.qty)

        mainImage = findViewById<ImageView>(R.id.mainImage)

        val recyclerView = findViewById<RecyclerView>(R.id.listRcv)
        val linearlayout = LinearLayoutManager(this)
        linearlayout.orientation = LinearLayoutManager.HORIZONTAL

        val editBtn = findViewById<ImageView>(R.id.editBtn)
        editBtn.setOnClickListener {
            startActivity(Intent(this, EditProductActivity::class.java)
                .putExtra("Id", _id))
        }

        recyclerView.layoutManager = linearlayout

        _id = intent.getStringExtra("Id").toString()

        val db = FirebaseFirestore.getInstance()
        db.collection("PRODUCTS").document(_id).get().addOnSuccessListener { task ->
            _id = task.id
            _title = task.get("TITLE").toString()

            titleTxt.text = task.get("TITLE").toString()
            priceTxt.text = task.get("PRICE").toString()
            dicriptionTxt.text = Html.fromHtml(task.get("DESCRIPTION").toString())

            qty.isChecked = task.getBoolean("InStock") == true

            _businessID = task.get("BUSINESSID").toString()
            _category = task.get("CATEGORY").toString()
            _price = task.get("PRICE").toString()
            _description = task.get("DESCRIPTION").toString()
            _inStock = task.getBoolean("InStock") == true
            Glide.with(this).load(task.get("Image1").toString()).into(mainImage)
            list.add(ImageModel(task.get("Image1").toString()))
            list.add(ImageModel(task.get("Image2").toString()))
            list.add(ImageModel(task.get("Image3").toString()))
            list.add(ImageModel(task.get("Image4").toString()))

            val adapter = ImageAdapter(list)
            recyclerView.adapter = adapter
        }

        val deleteBtn = findViewById<ImageView>(R.id.deleteBtn)
        deleteBtn.setOnClickListener {
            showDeleteDialog()
        }

        titleTxt.text = _title
        priceTxt.text = _price
        dicriptionTxt.text = Html.fromHtml(_description)
        qty.isChecked = _inStock

        qty.setOnCheckedChangeListener { buttonView, isChecked ->
            val data = HashMap<String, Any>()
            data.put("InStock", isChecked)
            db.collection("PRODUCTS").document(_id).update(data)
        }
    }

    fun showDeleteDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_delete_entry, null)
        dialog.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val db = FirebaseFirestore.getInstance()

        val yesBtn = view.findViewById<Button>(R.id.yesBtn)
        yesBtn.setOnClickListener {
            db.collection("PRODUCTS").document(_id).delete()
            onBackPressed()
        }

        val noBtn = view.findViewById<Button>(R.id.noBtn)
        noBtn.setOnClickListener {
            dialog.hide()
        }

        val title = view.findViewById<TextView>(R.id.title)
        val disc = view.findViewById<TextView>(R.id.disc)

        title.text = "Delete Product?"
        disc.text = "Are you sure you want to delete this product? This can\\'t e undone"

        dialog.setContentView(view)
        dialog.show()
    }
}