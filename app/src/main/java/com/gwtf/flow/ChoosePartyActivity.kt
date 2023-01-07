package com.gwtf.flow

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_ANSWER
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.Utilites.IDGenrator
import com.gwtf.flow.Utilites.PermissionTracking
import com.gwtf.flow.Utilites.getDateTime
import com.gwtf.flow.adapter.ContactAdapter
import com.gwtf.flow.adapter.PartyAdapter
import com.gwtf.flow.model.BookModel
import com.gwtf.flow.model.ContactModel
import com.gwtf.flow.model.PartyModel
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class ChoosePartyActivity : AppCompatActivity() {

    val listContacts = ArrayList<ContactModel>()
    lateinit var adapter: ContactAdapter
    lateinit var notFound: View

    companion object {
        var name: String = ""
        var number: String = ""
        var id: String = ""

        var isSelected = false
        lateinit var list_contacts: RecyclerView
        lateinit var database: SqlDatabase
        lateinit var list_parties: RecyclerView
        lateinit var textView2: TextView
        lateinit var doneSelected: TextView
        lateinit var btn_addParty: CardView
        var list = ArrayList<PartyModel>()
        lateinit var partyAdadpter: PartyAdapter

        fun DoneButton() {
            if (isSelected) {
                doneSelected.setTextColor(Color.parseColor("#5dc7d6"))
                doneSelected.isEnabled = true
            } else {
                doneSelected.setTextColor(Color.parseColor("#505050"))
            }
        }

        fun getData() {
            list.clear()
            list = database.getParty()
            partyAdadpter = PartyAdapter(list)
            textView2.text = "Added Parties (" + list.size + ")"
            list_parties.adapter = partyAdadpter
            DoneButton()
        }

        var personName: String = ""
        var mobileNumber: String = ""

        fun addParty() {
            btn_addParty.callOnClick()
        }

    }

    val permissions = arrayOf(
        Manifest.permission.READ_CONTACTS
    )
    val permissionCode = 78

    override fun onBackPressed() {
        val intent8 = Intent().apply {
            putExtra("name", name)
            putExtra("number", number)
            putExtra("partyId", id)
        }
        setResult(Activity.RESULT_OK, intent8)
        super.onBackPressed()
    }

    lateinit var txt_contacts: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_party)
        database = SqlDatabase(this)
        btn_addParty = findViewById<CardView>(R.id.btn_addParty)
        txt_contacts = findViewById(R.id.txt_contacts)
        val btn_addparty = findViewById<LinearLayout>(R.id.btnaddparty)




        btn_addParty.setOnClickListener{
            showAddPartyDialog()
        }
        btn_addparty.setOnClickListener{
            showAddPartyDialog()
        }

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        notFound = findViewById(R.id.notFound)
        notFound.visibility = View.GONE
        list_parties = findViewById<RecyclerView>(R.id.list_parties)
        list_contacts = findViewById<RecyclerView>(R.id.list_contacts)
        doneSelected = findViewById(R.id.doneSelected)

        doneSelected.setOnClickListener {
            if(isSelected) {
                onBackPressed()
            }
        }

        textView2 = findViewById<TextView>(R.id.textView2)
        val layoutaManager = LinearLayoutManager(this)
        layoutaManager.orientation = LinearLayoutManager.VERTICAL
        list_parties.layoutManager = layoutaManager

        getData()

        var flot_new_party = findViewById<ImageView>(R.id.flot_new_party)
        var list_contacts = findViewById<RecyclerView>(R.id.list_contacts)
        flot_new_party.setOnClickListener {
            showAddPartyDialog()
        }

        var layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        list_contacts.layoutManager = layoutManager

        if(checkContactPermissions()){
            layoutManager = LinearLayoutManager(this)
            adapter = ContactAdapter(listContacts)
            getContactc()
        }

        val searchTxt = findViewById<EditText>(R.id.search_text)
        searchTxt.doOnTextChanged { text, start, before, count ->
            filter(searchTxt.text.toString())
        }

        list_contacts.isNestedScrollingEnabled = false
        list_parties.isNestedScrollingEnabled = false
    }

    private fun filter(text: String) {
        val filteredlist: ArrayList<PartyModel> = ArrayList()
        for (item in list) {
            if (item.name.toLowerCase().contains(text.toLowerCase()) || item.id.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            notFound.visibility = View.VISIBLE
        } else {
            partyAdadpter.filterList(filteredlist)
            notFound.visibility = View.GONE
        }
    }

    private fun getContactc() {
        listContacts.clear()
        val cursor = this.contentResolver
            .query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,

                    ),null,null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )
        while (cursor!!.moveToNext()){
            val contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val contactModel =  ContactModel(contactName,contactNumber)
            listContacts.add(contactModel)
        }
        txt_contacts.text = "You Have (" + listContacts.size + ") Contacts"
        val adadter = ContactAdapter(listContacts)
        adadter.notifyDataSetChanged()
        list_contacts.adapter = adadter
        cursor.close()
    }


    private fun checkContactPermissions():Boolean{
        if (PermissionTracking.hasCOntactPermissions(this)){
            return true
        }else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            EasyPermissions.requestPermissions(
                this,
                "You will need to accept the permission in order to run the application",
                100,
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS,
            )
            return true
        }else{
            return false
        }
    }

    fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        TODO("Not yet implemented")
    }

    fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this).build().show()
        }else{
            checkContactPermissions()
        }
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
        txt_partyName.setText(personName)
        txt_mobileNumber.setText(mobileNumber)
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