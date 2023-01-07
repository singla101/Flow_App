package com.gwtf.flow

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.gwtf.flow.adapter.BusinessTypeAdapter
import com.gwtf.flow.adapter.CardAdapter
import com.gwtf.flow.model.CardModel
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class BusinessCardActivity : AppCompatActivity() {

    companion object {
        lateinit var _businessName: TextView
        lateinit var _ownerName : TextView
        lateinit var shareCard: ConstraintLayout
        lateinit var _accountType : TextView
        lateinit var _number : TextView
        lateinit var _address : TextView
        lateinit var _image : ImageView

        lateinit var list: RecyclerView
        var data = ArrayList<CardModel>()
        lateinit var adapter: CardAdapter
        lateinit var businessName : EditText
        lateinit var ownerName : EditText
        lateinit var mobileNumber : EditText
        lateinit var address : EditText
        lateinit var businessType: Spinner
        var BusinesType : String = ""

        fun updateData () {
            _businessName.text = businessName.text.toString()
            _number.text = mobileNumber.text.toString()
            _ownerName.text = ownerName.text.toString()
            _address.text = address.text.toString()
            _accountType.text = BusinesType
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_card)
        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener { onBackPressed() }

        list = findViewById(R.id.list_Remark)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        list.layoutManager = layoutManager

        _businessName = findViewById<TextView>(R.id.BusinessNameCAD)
        _ownerName = findViewById<TextView>(R.id.OwnerNameCAD)
        _accountType = findViewById<TextView>(R.id.AccountTypeCAD)
        _number = findViewById<TextView>(R.id.MobileNumberCAD)
        _address = findViewById<TextView>(R.id.AddressCAD)
        _image = findViewById<ImageView>(R.id.background)

        businessName = findViewById<EditText>(R.id.businessName)
        ownerName = findViewById<EditText>(R.id.ownerName)
        mobileNumber = findViewById<EditText>(R.id.MobileNumber)
        address = findViewById<EditText>(R.id.businessAddress)
        businessType = findViewById(R.id.businessSubCategory)

        shareCard = findViewById(R.id.shareCard)

        val types = resources.getStringArray(R.array.businessTypes)
        if (businessType != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, types)
            businessType.adapter = adapter

            businessType.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    if (businessType.selectedItem.toString().equals("Select Business"))
                        BusinesType = ""
                    else
                        BusinesType = businessType.selectedItem.toString()
                    updateData()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        updateData()

        businessName.doOnTextChanged { text, start, before, count ->
            updateData()
        }
        ownerName.doOnTextChanged { text, start, before, count ->
            updateData()
        }
        address.doOnTextChanged { text, start, before, count ->
            updateData()
        }
        mobileNumber.doOnTextChanged { text, start, before, count ->
            updateData()
        }

        data.clear()
        data.add(CardModel("", "", "", "", "", "#E6CE94", "Center" , "card4"))
        data.add(CardModel("", "", "", "", "", "#f3dd8f", "Right" , "card3"))
        data.add(CardModel("", "", "", "", "", "#ffffff", "Center" , "card6"))
        data.add(CardModel("", "", "", "", "", "#ffffff", "Center" , "card7"))
        data.add(CardModel("", "", "", "", "", "#E6CE94", "Left" , "card5"))
        data.add(CardModel("", "", "", "", "", "#ffffff", "Center" , "card1"))
        data.add(CardModel("", "", "", "", "", "#E6CE94", "Center" , "card2"))


        adapter = CardAdapter(data)
        list.adapter = adapter

        val saveBtn = findViewById<Button>(R.id.saveBtn)
        val shareBtn = findViewById<Button>(R.id.shareBtn)
        saveBtn.setOnClickListener {
            val path = getExternalFilesDir(null)!!.absolutePath + "/" + "BusinessCad" + ".jpg"
            var bitmap = Bitmap.createBitmap(shareCard.width, shareCard.height, Bitmap.Config.ARGB_8888)
            var canvas = Canvas(bitmap)
            shareCard.draw(canvas)
            val imgfile = File(path)
            val outputStream = FileOutputStream(imgfile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            isExternalStorageWritable(bitmap, "Save")
        }

        shareBtn.setOnClickListener {
            val path = getExternalFilesDir(null)!!.absolutePath + "/" + "BusinessCad" + ".jpg"
            var bitmap = Bitmap.createBitmap(shareCard.width, shareCard.height, Bitmap.Config.ARGB_8888)
            var canvas = Canvas(bitmap)
            shareCard.draw(canvas)
            val imgfile = File(path)
            val outputStream = FileOutputStream(imgfile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            isExternalStorageWritable(bitmap, "")
        }
    }

    private fun isExternalStorageWritable(b: Bitmap, type: String): Boolean {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            try {
                val imagesFolder = File(cacheDir, "images")
                val uri: Uri?
                imagesFolder.mkdirs()
                val file = File(imagesFolder, "shared_image.png")

                val stream = FileOutputStream(file)
                b.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.flush()
                stream.close()
                uri = FileProvider.getUriForFile(this, packageName+".fileprovider", file)
                if (type.equals("Save")) {

                } else {
                    share(uri, type)
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return true
        }
        return false
    }

    private fun share(uri: Uri?, type: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/*"
        startActivity(intent)
    }
}