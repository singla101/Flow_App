package com.gwtf.flow

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.ContactsContract.Directory
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.parser.IntegerParser
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.Query
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.Utilites.AmountCalculator
import com.gwtf.flow.Utilites.Constants
import com.gwtf.flow.Utilites.Constants.*
import com.gwtf.flow.Utilites.IDGenrator
import com.gwtf.flow.Utilites.getDateTime
import com.gwtf.flow.adapter.BooksAdapter
import com.gwtf.flow.adapter.BooksReportAdapter
import com.gwtf.flow.adapter.BusinessAdapter
import com.gwtf.flow.model.BookModel
import com.gwtf.flow.model.BusinessModel

class HomeFragment : Fragment() {

    lateinit var businessAdapter: BusinessAdapter
    lateinit var list_books: RecyclerView
    lateinit var v: Context
    lateinit var db: SqlDatabase
    lateinit var layout_no_book: View
    lateinit var txt_incash: TextView
    lateinit var txt_outcash: TextView
    lateinit var txt_total: TextView
    lateinit var layout_books: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        db = SqlDatabase(view.context)

        val include: View = view.findViewById(R.id.include)
        include.setOnClickListener {
            bottomDialog(view.context)
        }

        val upiButton = view.findViewById<LinearLayout>(R.id.upiButton)
        upiButton.setOnClickListener {
            startActivity(Intent(view.context, UPIActivity::class.java))
        }

        val gstCalculate = view.findViewById<LinearLayout>(R.id.gstCalculate)
        gstCalculate.setOnClickListener {
            startActivity(Intent(view.context, GSTCalculateActivity::class.java))
        }

        val businessCard = view.findViewById<LinearLayout>(R.id.businessCard)
        businessCard.setOnClickListener {
            startActivity(Intent(view.context, BusinessCardActivity::class.java))
        }

        v = view.context

        val sharedPref = view.context.getSharedPreferences(view.context.packageName, AppCompatActivity.MODE_PRIVATE)
        Constants.Business_Selected = sharedPref.getString("BusinessSelected", "")
        Constants.Business_Name = sharedPref.getString("BusinessName", "")
        Constants.Business_Image = sharedPref.getString("BusinessImage", "")

        val imageView = view.findViewById<ImageView>(R.id.image)
        if(Constants.Business_Image.equals("") || Business_Image.isEmpty()) {
            imageView.setImageResource(R.drawable.logo)
        } else {
            Glide.with(view.context).load(Business_Image).into(imageView)
        }

        val txt_business_name: TextView = view.findViewById(R.id.txt_business_name)
        txt_business_name.text = Business_Name

        layout_no_book = view.findViewById<View>(R.id.layout_no_book)
        layout_books = view.findViewById<LinearLayout>(R.id.layout_books)

        txt_incash = view.findViewById<TextView>(R.id.txt_incash)
        txt_outcash = view.findViewById<TextView>(R.id.txt_outcash)
        txt_total = view.findViewById<TextView>(R.id.txt_total)

        val btnFilter = view.findViewById<ImageView>(R.id.btnFilter)
        btnFilter.setOnClickListener {
            showFilterDialog()
        }

        val btnSearch = view.findViewById<ImageView>(R.id.btnSearch)
        btnSearch.setOnClickListener {
            startActivity(Intent(view.context, FindBookActivity::class.java))
        }

        val bttnCard1 = view.findViewById<CardView>(R.id.btn_card1)
        val bttnCard2 = view.findViewById<CardView>(R.id.btn_card2)

        bttnCard1.setOnClickListener {
            startActivity(Intent(view.context, ReportActivity::class.java))
        }

        val partiesBtn = view.findViewById<LinearLayout>(R.id.partiesBtn)
        partiesBtn.setOnClickListener {
            startActivity(Intent(view.context, PartiesActivity::class.java))
        }

        bttnCard2.setOnClickListener {
            startActivity(Intent(view.context, ReportActivity::class.java))
        }

        val btn_add_book = view.findViewById<Button>(R.id.btn_add_book)
        val flot_add_book = view.findViewById<FloatingActionButton>(R.id.flot_add_book)
        btn_add_book.isEnabled = true
        btn_add_book.setOnClickListener {
            bottomBookDialog()
        }

        flot_add_book.setOnClickListener {
            bottomBookDialog()
        }

        list_books = view.findViewById<RecyclerView>(R.id.list_books)
        val layManager = GridLayoutManager(view.context, 1)
        layManager.orientation = LinearLayoutManager.VERTICAL
        list_books.layoutManager = layManager

        getBooksData()


        return view
    }

    /// Get Data
    var list = ArrayList<BookModel>()

    override fun onResume() {
        super.onResume()
        getBooksData()
    }

    fun getBooksData() {
        txt_incash.text = "" + AmountCalculator.getIn(context)
        txt_outcash.text = "" + AmountCalculator.getOut(context)
        txt_total.text = "" + (AmountCalculator.getIn(context) - AmountCalculator.getOut(context))

        if (db.getBooks(Business_Selected).size > 0) {
            layout_no_book.visibility = View.GONE
            layout_books.visibility = View.VISIBLE
        } else {
            layout_no_book.visibility = View.VISIBLE
            layout_books.visibility = View.GONE
        }
        list.clear()
        list = db.getBooks(Business_Selected)
        val listAdapter = BooksAdapter(list)
        list_books.adapter = listAdapter
    }

    // Business Bottom Dialog
    fun bottomDialog(v: Context): Boolean {
        val dialog = BottomSheetDialog(v)
        val view = layoutInflater.inflate(R.layout.bottom_business_switch, null)
        dialog.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val db = SqlDatabase(v)

        val list_business = view.findViewById<RecyclerView>(R.id.list_business)
        val btn_add_business = view.findViewById<Button>(R.id.btn_add_business)
        val laManager = GridLayoutManager(view.context, 1)
        laManager.orientation = LinearLayoutManager.VERTICAL
        list_business.layoutManager = laManager

        var businessData = ArrayList<BusinessModel>()
        businessData = db.getBusiness()
        businessAdapter = BusinessAdapter(businessData)

        list_business.adapter = businessAdapter

        btn_add_business.setOnClickListener {
            startActivity(Intent(view.context, AddNewBusinessActivity::class.java))
        }

        val btn_close = view.findViewById<ImageView>(R.id.btn_close)
        btn_close.setOnClickListener {
            dialog.hide()
        }

        dialog.setContentView(view)
        dialog.show()
        return true
    }

    fun bottomBookDialog(): Boolean {
        val dialog = BottomSheetDialog(v)
        val view = layoutInflater.inflate(R.layout.bottom_add_new_book, null)
        dialog.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val txt_bookname = view.findViewById<EditText>(R.id.txt_bookname)
        val btn_add_book = view.findViewById<Button>(R.id.btn_add_book)
        txt_bookname.requestFocus()

        val btn_close = view.findViewById<ImageView>(R.id.btn_close)
        btn_close.setOnClickListener {
            dialog.hide()
        }

        btn_add_book.setOnClickListener {
            if (txt_bookname.text.toString().equals("") && txt_bookname.text.toString().equals(null)) {
                Toast.makeText(view.context, "Enter Book Name", Toast.LENGTH_SHORT).show()
            } else {
                val db = SqlDatabase(v)
                val id = IDGenrator.getId("BOOK")
                db.addBooks(id, txt_bookname.text.toString(),
                    getDateTime.getMilies(), Business_Selected)
                db.addCategories(id, "Salary")
                db.addCategories(id, "Sale")
                db.addCategories(id, "Bonus")
                db.addCategories(id, "Profit")
                db.addCategories(id, "Rent")

                Toast.makeText(view.context, "Book Successfully Created", Toast.LENGTH_SHORT).show()
                dialog.hide()
                getBooksData()
            }
        }

        dialog.setContentView(view)
        dialog.show()
        return true
    }

    fun showFilterDialog() {
        val dialog = BottomSheetDialog(v)
        val view = layoutInflater.inflate(R.layout.bottom_filter, null)
        dialog.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val lastUpdate = view.findViewById<RadioButton>(R.id.lastUpdate)
        val name = view.findViewById<RadioButton>(R.id.name)
        val netBalance = view.findViewById<RadioButton>(R.id.netBalance)
        val netBalancelow = view.findViewById<RadioButton>(R.id.netBalancelow)
        val lastCreated = view.findViewById<RadioButton>(R.id.lastCreated)

        lastUpdate.setOnClickListener {
            name.isChecked = false
            lastCreated.isChecked = false
            lastUpdate.isChecked = true
            netBalance.isChecked = false

            list.sortByDescending {
                it.date
            }

            val listAdapter = BooksAdapter(list)
            list_books.adapter = listAdapter
        }

        name.setOnClickListener {
            name.isChecked = true
            lastCreated.isChecked = false
            lastUpdate.isChecked = false
            netBalance.isChecked = false

            list.sortBy {
                it.name
            }
            val listAdapter = BooksAdapter(list)
            list_books.adapter = listAdapter
        }

        netBalance.setOnClickListener {
            name.isChecked = false
            lastCreated.isChecked = false
            lastUpdate.isChecked = false
            netBalance.isChecked = true

            list.sortByDescending {
                it.name
            }
            val listAdapter = BooksAdapter(list)
            list_books.adapter = listAdapter
        }

        lastCreated.setOnClickListener {
            name.isChecked = false
            lastCreated.isChecked = true
            lastUpdate.isChecked = false
            netBalance.isChecked = false

            getBooksData()
        }

        val btn_close = view.findViewById<ImageView>(R.id.btn_close)
        btn_close.setOnClickListener {
            dialog.hide()
        }

        dialog.setContentView(view)
        dialog.show()
    }
}