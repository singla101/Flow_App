package com.gwtf.flow

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.Radio
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.TimePicker
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.Utilites.Constants
import com.gwtf.flow.Utilites.IDGenrator
import com.gwtf.flow.Utilites.getDateTime
import com.gwtf.flow.adapter.SingleAdapter
import com.gwtf.flow.model.SingleModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditEntryActivity : AppCompatActivity() {

    lateinit var BookId: String
    var CashIn: Boolean = false

    lateinit var txt_date: TextView
    lateinit var txt_time: TextView
    lateinit var txt_partyName: EditText
    lateinit var txt_remark: EditText
    lateinit var txt_category: EditText
    var cal = Calendar.getInstance()

    companion object {
        lateinit var list_PaymentMode: RecyclerView
        var paymentMode: String = ""
        const val ACTIVITY_PARTY_REQUEST_CODE = 0
        lateinit var database: SqlDatabase

        fun updatePayment() {
            var lst = ArrayList<SingleModel>()
            lst.clear()
            lst = database.getPaymentMode()
            lst.add(SingleModel("Cash"))
            val adapter = SingleAdapter(lst)
            list_PaymentMode.adapter = adapter
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
        setContentView(R.layout.activity_edit_entry)

        _id = intent.getStringExtra("id").toString()
        BookId = intent.getStringExtra("id").toString()
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

        database = SqlDatabase(this)

        val txtAmount = findViewById<EditText>(R.id.txt_amount)
        val layout_details = findViewById<View>(R.id.layout_details)
        val saveBtn = findViewById<View>(R.id.saveBtn)
        val txt_title = findViewById<TextView>(R.id.txt_title)
        val deleteEntry = findViewById<ImageView>(R.id.deleteEntry)
        txt_date = findViewById(R.id.txt_date)
        txt_time = findViewById(R.id.txt_time)
        txt_partyName = findViewById(R.id.txt_partyName)
        txt_remark = findViewById(R.id.txt_remark)
        txt_category = findViewById(R.id.txt_category)
        list_PaymentMode = findViewById(R.id.list_PaymentMode)
        list_PaymentMode.isNestedScrollingEnabled = false

        deleteEntry.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.dialog_delete_entry, null)
            val yesBtn = view.findViewById<Button>(R.id.yesBtn)
            val noBtn = view.findViewById<Button>(R.id.noBtn)

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

        layout_details.visibility = View.GONE

        txt_partyName.setOnClickListener {
            ChoosePartyActivity.name = ""
            ChoosePartyActivity.number = ""
            ChoosePartyActivity.isSelected = false
            val intent = Intent(this, ChoosePartyActivity::class.java)
            startActivityForResult(intent, ACTIVITY_PARTY_REQUEST_CODE)
        }

        val pmLayoutmanager = FlexboxLayoutManager(this)
        pmLayoutmanager.flexDirection = FlexDirection.ROW
        list_PaymentMode.layoutManager = pmLayoutmanager

        updatePayment()

        txt_category.isFocusable = false
        txt_category.setOnClickListener {
            ChooseActivityActivity.id = ""
            ChooseActivityActivity.categoy = ""
            ChooseActivityActivity.isSelected = false
            val intent = Intent(this, ChooseActivityActivity::class.java).putExtra("BookId", BookId)
            startActivityForResult(intent, 2)
        }

        txtAmount.doOnTextChanged { text, start, before, count ->
            if (txtAmount.text.length > 0) {
                layout_details.visibility = View.VISIBLE
            } else {
                layout_details.visibility = View.GONE
            }
        }

        val cashIn = findViewById<RadioButton>(R.id.cashIn)
        val cashOut = findViewById<RadioButton>(R.id.cashOut)

        if (_paymentType.equals("IN"))
            cashIn.isChecked = true
        else
            cashOut.isChecked = true

        cashIn.setOnCheckedChangeListener { buttonView, isChecked ->
            cashOut.isChecked = !isChecked
        }

        cashOut.setOnCheckedChangeListener { buttonView, isChecked ->
            cashIn.isChecked = !isChecked
        }

        saveBtn.setOnClickListener {
            if (cashIn.isChecked)
                database.updateData(
                    _id,
                    _bookId, _bookId, txt_partyName.text.toString(), txt_partyName.text.toString(),
                    Constants.Business_Selected, txtAmount.text.toString().toInt(), txt_date.text.toString(), txt_time.text.toString(),
                    txt_remark.text.toString(), ChoosePartyActivity.number.toString(), txt_category.text.toString(),
                    paymentMode, "IN", "Payed")

            if  (cashOut.isChecked)
                database.updateData(
                    _id,
                    _bookId, _bookId, txt_partyName.text.toString(), txt_partyName.text.toString(),
                    Constants.Business_Selected, txtAmount.text.toString().toInt(), txt_date.text.toString(), txt_time.text.toString(),
                    txt_remark.text.toString(), ChoosePartyActivity.number.toString(), txt_category.text.toString(),
                    paymentMode, "OUT", "Payed")

            startActivity(Intent(this, SuccessActivity::class.java))
            finish()
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            txt_date.text = sdf.format(cal.time)
        }

        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                txt_time.setText(String.format("%2d:%2d ", hourOfDay, minute))
            }
        }, hour, minute, false)

        txt_date.setOnClickListener {
            mTimePicker.show()
        }

        txt_date.text = "" + getDateTime.getDate()
        txt_time.text = "" + getDateTime.getTimes()

        txt_time.setOnClickListener {
            mTimePicker.show()
        }

        val btn_date = findViewById<LinearLayout>(R.id.btn_date)
        btn_date.setOnClickListener {
            DatePickerDialog(this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }



        txt_partyName.setText(_partyName.toString())
        txtAmount.setText(_amount.toString())
        txt_category.setText(_category.toString())
        txt_date.setText(_date.toString())
        txt_remark.setText(_remark.toString())
        txt_time.setText(_time.toString())
        paymentMode = _paymentMode
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_PARTY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val output = data!!.getStringExtra("name") + " (+91 " + data!!.getStringExtra("number") + ")"
                txt_partyName.setText("" + output + "")
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                val output = data!!.getStringExtra("category")
                txt_category.setText(output)
            }
        }
    }

}