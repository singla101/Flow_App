package com.gwtf.flow

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.R
import com.gwtf.flow.Utilites.Constants
import com.gwtf.flow.Utilites.Constants.Business_Selected
import com.gwtf.flow.Utilites.LoadingDialog
import java.security.Key

class CreateStoreActivity : AppCompatActivity() {

    lateinit var storeBasicDetials: View
    lateinit var storeContact: View
    lateinit var paymentGateway: View

    var _logo: String = ""
    lateinit var imgUri: String

    lateinit var _id: String
    lateinit var logo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_store)

        storeBasicDetials = findViewById(R.id.storeBasicDetials)
        storeContact = findViewById(R.id.storeContact)
        paymentGateway = findViewById(R.id.paymentGateway)

        storeBasicDetials.visibility = View.VISIBLE

        /// Basic Detials
        logo = findViewById(R.id.logo)
        logo.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 1)
        }

        val title_txt = findViewById<EditText>(R.id.title_txt)
        val txt_disciption = findViewById<EditText>(R.id.txt_disciption)
        val saveContact = findViewById<Button>(R.id.saveContact)

        val db = FirebaseFirestore.getInstance()
        val dialog = LoadingDialog(this)

        saveContact.setOnClickListener {
            dialog.start()
            val data = HashMap<String, Any>()
            data.put("LOGO", _logo)
            data.put("TITLE", title_txt.text.toString())
            data.put("ABOUT", txt_disciption.text.toString())

            db.collection("WEBSITE").add(data).addOnSuccessListener { doc ->
                _id = doc.id
                storeBasicDetials.visibility = View.GONE
                storeContact.visibility = View.VISIBLE
                dialog.dismiss()
            }.addOnFailureListener {
                Toast.makeText(this, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

//        state

        val arrStress = resources.getStringArray(R.array.states)

        val spinner = findViewById<Spinner>(R.id.state)
        val mobile_txt = findViewById<EditText>(R.id.mobile_txt)
        val email_txt = findViewById<EditText>(R.id.email_txt)
        val txt_address1 = findViewById<EditText>(R.id.txt_address1)
        val txt_address2 = findViewById<EditText>(R.id.txt_address2)
        val landmarkTxt = findViewById<EditText>(R.id.landmarkTxt)
        val postalCode = findViewById<EditText>(R.id.postalCode)
        val townTxt = findViewById<EditText>(R.id.townTxt)

            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, arrStress)
            spinner.adapter = adapter

        val nextGateway = findViewById<Button>(R.id.nextGateway)
        nextGateway.setOnClickListener {
            dialog.start()
            val data = HashMap<String, Any>()
            data.put("MOBILE", mobile_txt.text.toString())
            data.put("EMAIL", email_txt.text.toString())
            data.put("ADDRESS1", txt_address1.text.toString())
            data.put("ADDRESS2", txt_address2.text.toString())
            data.put("LANDMARK", landmarkTxt.text.toString())
            data.put("POSTAL", postalCode.text.toString())
            data.put("TOWN", townTxt.text.toString())

            db.collection("WEBSITE").document(_id).update(data).addOnSuccessListener { doc ->
                storeContact.visibility = View.GONE
                paymentGateway.visibility = View.VISIBLE
                dialog.dismiss()
            }.addOnFailureListener {
                Toast.makeText(this, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        //
        val razorpaySetKEy = findViewById<EditText>(R.id.razorpaySetKEy)
        val razorpayKey = findViewById<EditText>(R.id.razorpayKey)
        val paytmAppKey = findViewById<EditText>(R.id.paytmAppKey)
        val paytmSecretKey = findViewById<EditText>(R.id.paytmSecretKey)
        val saveGateway = findViewById<Button>(R.id.saveGateway)

        saveGateway.setOnClickListener {
            dialog.dismiss()
            val data = HashMap<String, Any>()
            data.put("REZORPAYKEY", razorpayKey.text.toString())
            data.put("RAZORPAYSECKEY", razorpaySetKEy.text.toString())
            data.put("PAYTMAPPKEY", paytmAppKey.text.toString())
            data.put("PAYTMSECKEY", paytmSecretKey.text.toString())

            if (razorpayKey.text.isEmpty() || razorpaySetKEy.text.isEmpty())
                data.put("RAZORPAY", false)
            else if (paytmAppKey.text.isEmpty() || paytmSecretKey.text.isEmpty())
                data.put("PAYTM", false)

            db.collection("WEBSITE").document(_id).update(data).addOnSuccessListener { doc ->
                val share = getSharedPreferences(packageName, MODE_PRIVATE)
                val edit = share.edit()
                edit.putBoolean(Business_Selected + "_WEBSITE", true)
                edit.apply()
                onBackPressed()
                dialog.dismiss()
            }.addOnFailureListener {
                Toast.makeText(this, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val sharedPref = getSharedPreferences(packageName, AppCompatActivity.MODE_PRIVATE)
        if (resultCode == RESULT_OK && requestCode == 1) {
            imgUri = data?.data.toString()
            val imageUri: Uri = data?.data as Uri
            // Firebase Storage
            uploadImage(imageUri)
        }
    }

    fun uploadImage(filePath: Uri){
        val storageReference = FirebaseStorage.getInstance().reference
        if(filePath != null){
            val sharedPref = getSharedPreferences(packageName, AppCompatActivity.MODE_PRIVATE)
            val ref = storageReference.child("USER/WEBSITE/" + Constants.Business_Selected + "/" + "LOGO")
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
                    val downloadUri = task.result
                    // put link
                    _logo = downloadUri.toString()
                    Glide.with(this).load(downloadUri.toString()).into(logo)
                }
            }
        }else{
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }
}