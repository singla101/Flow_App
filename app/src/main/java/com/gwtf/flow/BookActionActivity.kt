package com.gwtf.flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.Utilites.Constants.Business_Selected
import com.gwtf.flow.adapter.BusinessAdapter
import com.gwtf.flow.adapter.BusinessTypeAdapter
import com.gwtf.flow.adapter.SelectBooksAdapter
import com.gwtf.flow.adapter.SelectBusinessAdapter
import com.gwtf.flow.model.BookModel
import com.gwtf.flow.model.BusinesModel
import com.gwtf.flow.model.BusinessModel

class BookActionActivity : AppCompatActivity() {

    companion object {
        var BusinessId: String = ""
        var isSelected: Boolean = false
        lateinit var rvBusiness: RecyclerView
        lateinit var adapter: SelectBooksAdapter
        var list = ArrayList<BookModel>()
        lateinit var db: SqlDatabase

        fun getData() {
            list.clear()
            list = db.getBooks(Business_Selected)
            adapter = SelectBooksAdapter(list)
            rvBusiness.adapter = adapter
        }
    }

    lateinit var _id: String
    lateinit var _amount: String
    lateinit var _date: String
    lateinit var _number: String
    lateinit var _remark: String
    lateinit var _bookName: String
    lateinit var _businessId: String
    lateinit var _partyId: String
    lateinit var _bookId: String
    lateinit var _category: String
    lateinit var _partyName: String
    lateinit var _paymentStatus: String
    lateinit var _paymentMode: String
    lateinit var _paymentType: String
    lateinit var _time: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_action)
        db = SqlDatabase(this)
        _id = intent.getStringExtra("id").toString()
        _amount = intent.getStringExtra("amount").toString()
        _date = intent.getStringExtra("date").toString()
        _number = intent.getStringExtra("number").toString()
        _remark = intent.getStringExtra("remark").toString()
        _bookName = intent.getStringExtra("BOOKNAME").toString()
        _businessId = intent.getStringExtra("BUSINESSID").toString()
        _partyId = intent.getStringExtra("PARTYID").toString()
        _bookId = intent.getStringExtra("bookid").toString()
        _category = intent.getStringExtra("category").toString()
        _partyName = intent.getStringExtra("partyName").toString()
        _paymentStatus = intent.getStringExtra("paymentStatus").toString()
        _paymentMode = intent.getStringExtra("paymentMode").toString()
        _paymentType = intent.getStringExtra("paymentType").toString()
        _time = intent.getStringExtra("time").toString()

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        BusinessId = _bookId

        rvBusiness = findViewById(R.id.listBusiness)
        val titleTxt = findViewById<TextView>(R.id.titleTxt)
        val saveBtn = findViewById<Button>(R.id.saveBtn)

        val layoutManager = GridLayoutManager(this, 1)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvBusiness.layoutManager = layoutManager
        getData()

        if (intent.getStringExtra("ACTION").equals("Move")) {
            titleTxt.text = "Move Entry"
        } else if (intent.getStringExtra("ACTION").equals("COPY")) {
            titleTxt.text = "Copy Entry"
        } else if (intent.getStringExtra("ACTION").equals("COPYOP")) {
            titleTxt.text = "Copy Opposite Entry"
        }

        saveBtn.setOnClickListener {
            if (intent.getStringExtra("ACTION").equals("Move")) {
                db.updateData(_id, _bookName, BusinessId, _partyName, _partyId, _businessId, _amount.toInt(), _date, _time, _remark, _number, _category, _paymentMode, _paymentType, _paymentStatus)
            } else if (intent.getStringExtra("ACTION").equals("COPY")) {
                db.addData(_id, _bookName, BusinessId, _partyName, _partyId, _businessId, _amount.toInt(), _date, _time, _remark, _number, _category, _paymentMode, _paymentType, _paymentStatus)
            } else if (intent.getStringExtra("ACTION").equals("COPYOP")) {
                if (_paymentType.equals("IN"))
                    db.addData(_id, _bookName, BusinessId, _partyName, _partyId, _businessId,
                        _amount.toInt(), _date, _time, _remark, _number, _category, _paymentMode,
                        "OUT", _paymentStatus)
                else
                    db.addData(_id, _bookName, BusinessId, _partyName, _partyId, _businessId,
                        _amount.toInt(), _date, _time, _remark, _number, _category, _paymentMode,
                        "IN", _paymentStatus)
            }

            onBackPressed()
            finish()
        }

    }
}