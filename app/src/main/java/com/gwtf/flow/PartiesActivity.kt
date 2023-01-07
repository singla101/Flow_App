package com.gwtf.flow

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony.Mms.Part
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.helper.widget.Carousel.Adapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.Utilites.IDGenrator
import com.gwtf.flow.Utilites.getDateTime
import com.gwtf.flow.adapter.PartitesAdapter
import com.gwtf.flow.model.PartyModel

class PartiesActivity : AppCompatActivity() {

    lateinit var rcv_parties: RecyclerView
    lateinit var adapter: PartitesAdapter
    var list = ArrayList<PartyModel>()
    lateinit var db: SqlDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parties)
        db = SqlDatabase(this)
        rcv_parties = findViewById(R.id.listBusiness)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        val layoutManager = GridLayoutManager(this, 1)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rcv_parties.layoutManager = layoutManager

        getData()

        val flotAddParty = findViewById<FloatingActionButton>(R.id.flotAddParty)
        flotAddParty.setOnClickListener {
            showAddPartyDialog()
        }
    }

    fun getData() {
        list = db.party
        adapter = PartitesAdapter(list)
        rcv_parties.adapter = adapter
    }


    fun showAddPartyDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_add_new_party, null)
        dialog.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val db = SqlDatabase(this)

        val btn_close = view.findViewById<ImageView>(R.id.btn_close)
        val txt_partyName = view.findViewById<EditText>(R.id.txt_partyName)
        val txt_mobileNumber = view.findViewById<EditText>(R.id.txt_mobileNumber)
        val txt_email = view.findViewById<EditText>(R.id.txt_email)

        txt_partyName.requestFocus()

        val btn_customer = view.findViewById<CardView>(R.id.btn_customer)
        val btn_supplier = view.findViewById<CardView>(R.id.btn_supplier)

        val customerTxt = view.findViewById<TextView>(R.id.customerTxt)
        val supplierTxt = view.findViewById<TextView>(R.id.supplierTxt)

        val btn_add_book = view.findViewById<Button>(R.id.btn_add_book)

        var type = "";
        btn_customer.setOnClickListener {
            type = "Customer"
            customerTxt.setTextColor(Color.parseColor("#000000"))
            supplierTxt.setTextColor(Color.parseColor("#ffffff"))
        }

        btn_supplier.setOnClickListener {
            type = "Supplier"
            supplierTxt.setTextColor(Color.parseColor("#000000"))
            customerTxt.setTextColor(Color.parseColor("#ffffff"))
        }

        btn_add_book.setOnClickListener {
            if (txt_partyName.text.isNotEmpty()) {
                db.addParty(
                    IDGenrator.getId("PARTY"),
                    txt_partyName.text.toString(),
                    txt_mobileNumber.text.toString(),
                    txt_email.text.toString(),
                    type,
                    getDateTime.getMilies(),
                )
                dialog.hide()
                getData()
            } else {
                txt_partyName.error = "Enter valid Party Name"
            }
        }

        btn_close.setOnClickListener {
            dialog.hide()
        }

        dialog.setContentView(view)
        dialog.show()
    }
}