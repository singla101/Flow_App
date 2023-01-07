package com.gwtf.flow

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.api.Distribution.BucketOptions.Linear
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.adapter.BusinessAdapter
import com.gwtf.flow.model.BusinessModel
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class EntryViewActivity : AppCompatActivity() {

    lateinit var amount: String
    lateinit var name: String
    lateinit var _id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_view)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        val entryType = findViewById<LinearLayout>(R.id.entryType)
        val entryTypeTxt = findViewById<TextView>(R.id.entryTypeTxt)
        val dateTimeTxt = findViewById<TextView>(R.id.dateTimeTxt)
        val paymentTxt = findViewById<TextView>(R.id.paymentTxt)
        val customerNameTxt = findViewById<TextView>(R.id.customerNameTxt)
        val numberTxt = findViewById<TextView>(R.id.numberTxt)
        val remarkTxt = findViewById<TextView>(R.id.remarkTxt)
        val mode = findViewById<TextView>(R.id.mode)

        if (intent.getStringExtra("paymentType").equals("IN")) {
            paymentTxt.setTextColor(Color.parseColor("#4CAF50"))
            entryType.setBackgroundColor(Color.parseColor("#4CAF50"))
            entryTypeTxt.text = "Cash In"
        } else {
            paymentTxt.setTextColor(Color.parseColor("#B0372E"))
            entryType.setBackgroundColor(Color.parseColor("#B0372E"))
            entryTypeTxt.text = "Cash Out"
        }

        dateTimeTxt.text = intent.getStringExtra("date") + " " + intent.getStringExtra("time")
        paymentTxt.text = intent.getStringExtra("amount")
        _id = intent.getStringExtra("id").toString()
        customerNameTxt.text = intent.getStringExtra("partyName")
        numberTxt.text = intent.getStringExtra("partyName").toString().replace(Regex("[^0-9]"), "")
        remarkTxt.text = intent.getStringExtra("remark")
        amount = intent.getStringExtra("amount").toString()
        name = intent.getStringExtra("partyName").toString()
        mode.text = intent.getStringExtra("paymentMode")

        val editBtn = findViewById<TextView>(R.id.txt_edit)
        editBtn.setOnClickListener {
            startActivity(Intent(this, EditEntryActivity::class.java)
                .putExtra("id", intent.getStringExtra("id"))
                .putExtra("amount", intent.getStringExtra("amount"))
                .putExtra("date", intent.getStringExtra("date"))
                .putExtra("number", intent.getStringExtra("number"))
                .putExtra("remark", intent.getStringExtra("remark"))
                .putExtra("BOOKNAME", intent.getStringExtra("BOOKNAME"))
                .putExtra("BUSINESSID", intent.getStringExtra("BUSINESSID"))
                .putExtra("PARTYID", intent.getStringExtra("PARTYID"))
                .putExtra("bookid", intent.getStringExtra("bookid"))
                .putExtra("category", intent.getStringExtra("category"))
                .putExtra("partyName", intent.getStringExtra("partyName"))
                .putExtra("paymentStatus", intent.getStringExtra("paymentStatus"))
                .putExtra("paymentMode", intent.getStringExtra("paymentMode"))
                .putExtra("paymentType", intent.getStringExtra("paymentType"))
                .putExtra("time", intent.getStringExtra("time"))
            )
            finish()
        }

        val moreBtn = findViewById<ImageView>(R.id.moreBtn)
        moreBtn.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_actions, null)
            dialog.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

            val deleteEntry = view.findViewById<LinearLayout>(R.id.deleteEntry)
            val moveBtn = view.findViewById<LinearLayout>(R.id.moveBtn)
            val copyBtn = view.findViewById<LinearLayout>(R.id.copyBtn)
            val copyOP = view.findViewById<LinearLayout>(R.id.copyOP)
            deleteEntry.setOnClickListener {
                deleteEntry()
            }

            moveBtn.setOnClickListener {
                startActivity(Intent(this, BookActionActivity::class.java)
                    .putExtra("id", intent.getStringExtra("id"))
                    .putExtra("amount", intent.getStringExtra("amount"))
                    .putExtra("date", intent.getStringExtra("date"))
                    .putExtra("number", intent.getStringExtra("number"))
                    .putExtra("remark", intent.getStringExtra("remark"))
                    .putExtra("BOOKNAME", intent.getStringExtra("BOOKNAME"))
                    .putExtra("BUSINESSID", intent.getStringExtra("BUSINESSID"))
                    .putExtra("PARTYID", intent.getStringExtra("PARTYID"))
                    .putExtra("bookid", intent.getStringExtra("bookid"))
                    .putExtra("category", intent.getStringExtra("category"))
                    .putExtra("partyName", intent.getStringExtra("partyName"))
                    .putExtra("paymentStatus", intent.getStringExtra("paymentStatus"))
                    .putExtra("paymentMode", intent.getStringExtra("paymentMode"))
                    .putExtra("paymentType", intent.getStringExtra("paymentType"))
                    .putExtra("time", intent.getStringExtra("time"))
                    .putExtra("ACTION", "Move"))
            }

            copyBtn.setOnClickListener {
                startActivity(Intent(this, BookActionActivity::class.java)
                    .putExtra("id", intent.getStringExtra("id"))
                    .putExtra("amount", intent.getStringExtra("amount"))
                    .putExtra("date", intent.getStringExtra("date"))
                    .putExtra("number", intent.getStringExtra("number"))
                    .putExtra("remark", intent.getStringExtra("remark"))
                    .putExtra("BOOKNAME", intent.getStringExtra("BOOKNAME"))
                    .putExtra("BUSINESSID", intent.getStringExtra("BUSINESSID"))
                    .putExtra("PARTYID", intent.getStringExtra("PARTYID"))
                    .putExtra("bookid", intent.getStringExtra("bookid"))
                    .putExtra("category", intent.getStringExtra("category"))
                    .putExtra("partyName", intent.getStringExtra("partyName"))
                    .putExtra("paymentStatus", intent.getStringExtra("paymentStatus"))
                    .putExtra("paymentMode", intent.getStringExtra("paymentMode"))
                    .putExtra("paymentType", intent.getStringExtra("paymentType"))
                    .putExtra("time", intent.getStringExtra("time"))
                    .putExtra("ACTION", "COPY"))
            }

            copyOP.setOnClickListener {
                startActivity(Intent(this, BookActionActivity::class.java)
                    .putExtra("id", intent.getStringExtra("id"))
                    .putExtra("amount", intent.getStringExtra("amount"))
                    .putExtra("date", intent.getStringExtra("date"))
                    .putExtra("number", intent.getStringExtra("number"))
                    .putExtra("remark", intent.getStringExtra("remark"))
                    .putExtra("BOOKNAME", intent.getStringExtra("BOOKNAME"))
                    .putExtra("BUSINESSID", intent.getStringExtra("BUSINESSID"))
                    .putExtra("PARTYID", intent.getStringExtra("PARTYID"))
                    .putExtra("bookid", intent.getStringExtra("bookid"))
                    .putExtra("category", intent.getStringExtra("category"))
                    .putExtra("partyName", intent.getStringExtra("partyName"))
                    .putExtra("paymentStatus", intent.getStringExtra("paymentStatus"))
                    .putExtra("paymentMode", intent.getStringExtra("paymentMode"))
                    .putExtra("paymentType", intent.getStringExtra("paymentType"))
                    .putExtra("time", intent.getStringExtra("time"))
                    .putExtra("ACTION", "COPYOP"))
            }

            dialog.setContentView(view)
            dialog.show()
        }

        val shareButton = findViewById<TextView>(R.id.shareButton)
        shareButton.setOnClickListener {
            showBottomDialog()
        }
    }

    fun deleteEntry() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_delete_entry, null)
        val yesBtn = view.findViewById<Button>(R.id.yesBtn)
        val noBtn = view.findViewById<Button>(R.id.noBtn)
        val database = SqlDatabase(this)
        yesBtn.setOnClickListener {
            database.deleteData(_id)
            dialog.hide()
            onBackPressed()
            finish()
        }

        noBtn.setOnClickListener {
            dialog.hide()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    fun showBottomDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_entry_share, null)
        dialog.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val entryType = view.findViewById<LinearLayout>(R.id.entryType)
        val dateTimeTxt = view.findViewById<TextView>(R.id.dateTimeTxt)
        val paymentTxt = view.findViewById<TextView>(R.id.paymentTxt)
        val customerNameTxt = view.findViewById<TextView>(R.id.customerNameTxt)
        val numberTxt = view.findViewById<TextView>(R.id.numberTxt)
        val remarkTxt = view.findViewById<TextView>(R.id.remarkTxt)
        val mode = view.findViewById<TextView>(R.id.mode)

        if (intent.getStringExtra("paymentType").equals("IN")) {
            paymentTxt.setTextColor(Color.parseColor("#4CAF50"))
            entryType.setBackgroundColor(Color.parseColor("#4CAF50"))
        } else {
            paymentTxt.setTextColor(Color.parseColor("#B0372E"))
            entryType.setBackgroundColor(Color.parseColor("#B0372E"))
        }

        dateTimeTxt.text = intent.getStringExtra("date") + " " + intent.getStringExtra("time")
        paymentTxt.text = intent.getStringExtra("amount")
        customerNameTxt.text = intent.getStringExtra("partyName")
        numberTxt.text = intent.getStringExtra("partyName").toString().replace(Regex("[^0-9]"), "")
        remarkTxt.text = intent.getStringExtra("remark")
        mode.text = intent.getStringExtra("paymentMode")

        val shareCard = view.findViewById<CardView>(R.id.shareLayout)

        val btn_add_book = view.findViewById<Button>(R.id.btn_add_book)
        btn_add_book.setOnClickListener {
            val now = intent.getStringExtra("partyName")
            val path = getExternalFilesDir(null)!!.absolutePath + "/" + now + ".jpg"
            var bitmap = Bitmap.createBitmap(shareCard.width, shareCard.height, Bitmap.Config.ARGB_8888)
            var canvas = Canvas(bitmap)
            shareCard.draw(canvas)
            val imgfile = File(path)
            val outputStream = FileOutputStream(imgfile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            isExternalStorageWritable(bitmap, "")
            dialog.cancel()
        }

        dialog.setContentView(view)
        dialog.show()
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
                share(uri, type)

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
        intent.putExtra(Intent.EXTRA_TEXT, "Dear " + name + ", your bill for amount " + amount + " is attached. You can generate you bills & track your business expenses using Flow. Download now play.google.com/store/apps/details?id=" + packageName)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/*"
        startActivity(intent)
    }

}