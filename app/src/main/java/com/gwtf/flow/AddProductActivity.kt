package com.gwtf.flow

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.R
import com.gwtf.flow.Utilites.Constants.Business_Selected
import com.gwtf.flow.Utilites.IDGenrator
import com.gwtf.flow.adapter.ImageAdapter
import com.gwtf.flow.model.ImageModel
import com.itextpdf.text.pdf.parser.Line
import java.util.Objects

class AddProductActivity : AppCompatActivity() {

    lateinit var txt_title: EditText
    lateinit var txt_price: EditText
    lateinit var txt_category: EditText
    lateinit var txt_dicription: EditText

    lateinit var btnSave: Button

    lateinit var db: SqlDatabase
    lateinit var store: FirebaseFirestore
    lateinit var productId: String

    lateinit var addImage: CardView
    lateinit var imageRcv: RecyclerView

    lateinit var imgUri: String

    var imgList = java.util.ArrayList<ImageModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        db = SqlDatabase(this)
        store = FirebaseFirestore.getInstance()

        txt_title = findViewById(R.id.txt_title)
        txt_price = findViewById(R.id.txt_price)
        txt_category = findViewById(R.id.txt_category)
        txt_dicription = findViewById(R.id.txt_dicription)
        imageRcv = findViewById(R.id.images)
        addImage = findViewById(R.id.addImage)

        val layoutManager = GridLayoutManager(this, 1)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        imageRcv.layoutManager = layoutManager

        btnSave = findViewById(R.id.btnSave)
        productId = IDGenrator.getId("PRODUCT")

        addImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 1)
        }

        btnSave.setOnClickListener {
            val data = HashMap<String, Any>()
            data.put("PRODUCTID", productId.toString())
            data.put("TITLE", txt_title.text.toString())
            data.put("BUSINESSID", Business_Selected)
            data.put("PRICE", txt_price.text.toString())
            data.put("CATEGORY", txt_category.text.toString())
            data.put("DESCRIPTION", txt_dicription.text.toString())
            data.put("InStock", true)

            for(i in imgList) {
                data.put("Image" + i, i.Image.toString())
            }

            store.collection("PRODUCTS").add(data).addOnSuccessListener {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val sharedPref = getSharedPreferences(packageName, AppCompatActivity.MODE_PRIVATE)
        if (resultCode == RESULT_OK && requestCode == 1) {
            imgUri = data?.data.toString()
            val imageUri: Uri = data?.data as Uri
            uploadImage(imageUri)
        }
    }

    fun uploadImage(filePath: Uri){
        val storageReference = FirebaseStorage.getInstance().reference
        if(filePath != null){
            val ref = storageReference.child("USER/PRODUCTS/" + productId + "/" + Business_Selected)
            val uploadTask = ref.putFile(filePath!!)

            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    imgUri = task.result.toString()
                    updateImageList()
                }
            }
        }else{
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateImageList () {
        imgList.add(ImageModel(imgUri))
        val adapter = ImageAdapter(imgList)
        imageRcv.adapter = adapter
    }
}