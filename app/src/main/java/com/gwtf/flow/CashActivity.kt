package com.gwtf.flow

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.Utilites.Constants.Business_Selected
import com.gwtf.flow.Utilites.IDGenrator
import com.gwtf.flow.Utilites.getDateTime
import com.gwtf.flow.adapter.SingleAdapter
import com.gwtf.flow.model.SingleModel
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CashActivity : AppCompatActivity() {

    lateinit var BookId: String
    var CashIn: Boolean = false
    var partId: String = ""

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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash)

        BookId = intent.getStringExtra("id").toString()
        CashIn = intent.getBooleanExtra("cash", false)
        database = SqlDatabase(this)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        val txtAmount = findViewById<EditText>(R.id.txt_amount)
        val layout_details = findViewById<View>(R.id.layout_details)
        val saveBtn = findViewById<View>(R.id.saveBtn)
        val txt_title = findViewById<TextView>(R.id.txt_title)
        txt_date = findViewById(R.id.txt_date)
        txt_time = findViewById(R.id.txt_time)
        txt_partyName = findViewById(R.id.txt_partyName)
        val settings: ImageView = findViewById(R.id.settings)
        txt_remark = findViewById(R.id.txt_remark)
        txt_category = findViewById(R.id.txt_category)
        list_PaymentMode = findViewById(R.id.list_PaymentMode)
        list_PaymentMode.isNestedScrollingEnabled = false

        settings.setOnClickListener {
            startActivity(Intent(this, CashSettingsActivity::class.java))
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

        saveBtn.setOnClickListener {
            if (CashIn)
                database.addData(IDGenrator.getId("DATA"),
                    BookId, BookId, txt_partyName.text.toString(), partId,
                    Business_Selected, txtAmount.text.toString().toInt(), txt_date.text.toString(), txt_time.text.toString(),
                    txt_remark.text.toString(), ChoosePartyActivity.number.toString(), txt_category.text.toString(),
                    paymentMode, "IN", "Payed")
            else
                database.addData(IDGenrator.getId("DATA"),
                    BookId, BookId, txt_partyName.text.toString(), partId,
                    Business_Selected, txtAmount.text.toString().toInt(), txt_date.text.toString(), txt_time.text.toString(),
                    txt_remark.text.toString(), ChoosePartyActivity.number.toString(), txt_category.text.toString(), paymentMode, "OUT", "Payed")

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

        txt_date.setOnClickListener {
            DatePickerDialog(this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        val btn_date = findViewById<LinearLayout>(R.id.btn_date)
        btn_date.setOnClickListener {
            DatePickerDialog(this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        if (CashIn) {
            txt_title.text = "Add Cash In Entry"
            txt_title.setTextColor(Color.parseColor("#4CAF50"))
            txtAmount.setTextColor(Color.parseColor("#4CAF50"))
        } else {
            txt_title.text = "Add Cash Out Entry"
            txt_title.setTextColor(Color.parseColor("#B0372E"))
            txtAmount.setTextColor(Color.parseColor("#B0372E"))
        }
        checkSettings()
    }

    override fun onResume() {
        super.onResume()
        checkSettings()
    }

    fun checkSettings() {
        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        if (sharedPreferences.getBoolean("isParty", true))
            txt_partyName.visibility = View.VISIBLE
        else
            txt_partyName.visibility = View.GONE

        if (sharedPreferences.getBoolean("isCategory", true))
            txt_category.visibility = View.VISIBLE
        else
            txt_category.visibility = View.GONE

        if (sharedPreferences.getBoolean("isMode", true))
            list_PaymentMode.visibility = View.VISIBLE
        else
            list_PaymentMode.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_PARTY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                partId = data!!.getStringExtra("partyId").toString()
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