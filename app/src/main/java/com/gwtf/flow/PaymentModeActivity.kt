package com.gwtf.flow

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.adapter.CategoryAdapter
import com.gwtf.flow.model.CategoriesModel

class PaymentModeActivity : AppCompatActivity() {

    companion object {
        var categoy: String = ""
        var id: String = ""
        var isSelected = false

        lateinit var list_categories: RecyclerView
        lateinit var database: SqlDatabase
        lateinit var doneSelected: TextView
        var BookId = ""

        fun DoneButton() {
            if (isSelected) {
                doneSelected.setTextColor(Color.parseColor("#5dc7d6"))
                doneSelected.isEnabled = true
            } else {
                doneSelected.setTextColor(Color.parseColor("#505050"))
            }
        }

        fun getData() {
            var list = ArrayList<CategoriesModel>()
            list.clear()
            list = database.getPaymentsMode()
            var adapter = CategoryAdapter(list)
            list_categories.adapter = adapter
            DoneButton()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_mode)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        doneSelected = findViewById(R.id.doneSelected)
        BookId = intent.getStringExtra("BookId").toString()

        list_categories = findViewById(R.id.list_categories)
        database = SqlDatabase(this)

        var layoutManager = GridLayoutManager(this, 1)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        list_categories.layoutManager = layoutManager

        getData()

        doneSelected.setOnClickListener {
            if (isSelected) {
                onBackPressed()
            }
        }

        val flotNew = findViewById<ImageView>(R.id.flot_new_party)
        flotNew.setOnClickListener {
            showAddPartyDialog()
        }
    }

    override fun onBackPressed() {
        val intent8 = Intent().apply {
            putExtra("category", categoy)
        }
        setResult(Activity.RESULT_OK, intent8)
        super.onBackPressed()
    }

    fun showAddPartyDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_add_new_category, null)
        dialog.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val db = SqlDatabase(this)

        val btn_close = view.findViewById<ImageView>(R.id.btn_close)
        val txt_partyName = view.findViewById<EditText>(R.id.txt_partyName)

        val btn_add_book = view.findViewById<Button>(R.id.btn_add_book)

        btn_add_book.setOnClickListener {
            if (txt_partyName.text.isNotEmpty()) {
                db.addCategories(
                    BookId,
                    txt_partyName.text.toString()
                )
                dialog.hide()
                getData()
            } else {
                txt_partyName.error = "Enter valid Category Name"
            }
        }

        btn_close.setOnClickListener {
            dialog.hide()
        }

        dialog.setContentView(view)
        dialog.show()
    }

}