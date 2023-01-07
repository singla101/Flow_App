package com.gwtf.flow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.View
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.Utilites.AmountCalculator
import com.gwtf.flow.adapter.DataAdapter
import com.gwtf.flow.model.BookModel
import com.gwtf.flow.model.DataModel

class BookActivity : AppCompatActivity() {

    lateinit var BookId: String
    lateinit var db: SqlDatabase
    lateinit var list_payments: RecyclerView
    lateinit var totalBooks: TextView
    lateinit var txt_total: TextView
    lateinit var txtIn: TextView
    lateinit var txtOut: TextView
    lateinit var txtTotal: TextView
    lateinit var notFound: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        BookId = intent.getStringExtra("id").toString()

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        val bookmore = findViewById<ImageView>(R.id.btn_bookmore)
        bookmore.setOnClickListener {
            val popupMenus = PopupMenu(this, bookmore)
            popupMenus.inflate(R.menu.menu_book_list)
            popupMenus.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_rename -> {
                        startActivity(
                            Intent(this, EditBookNameActivity::class.java)
                                .putExtra("BookID", BookId)
                        )
                        true
                    }
                    R.id.menu_deletebook -> {
                        startActivity(
                            Intent(this, DuplicateBookActivity::class.java)
                                .putExtra("id", BookId).putExtra("name", intent.getStringExtra("name")))
                        true
                    }
                    else -> false
                }
            }
            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)
        }

        notFound = findViewById(R.id.notFound)
        notFound.visibility = View.GONE

        txtIn = findViewById<TextView>(R.id.txt_incash)
        txtOut = findViewById<TextView>(R.id.txt_outcash)
        txtTotal = findViewById<TextView>(R.id.txt_total)

        txtIn.text = "" + AmountCalculator.getBookIn(this, BookId)
        txtOut.text = "" + AmountCalculator.getBookOut(this, BookId)
        txtTotal.text = "" + (AmountCalculator.getBookIn(this, BookId) - AmountCalculator.getBookOut(this, BookId))

        val btnCashIn = findViewById<Button>(R.id.btnCashIn)
        val btnCashOut = findViewById<Button>(R.id.btnCashOut)
        btnCashIn.setOnClickListener {
            startActivity(Intent(this, CashActivity::class.java)
                .putExtra("id", BookId).putExtra("cash", true))
        }

        btnCashOut.setOnClickListener {
            startActivity(Intent(this, CashActivity::class.java)
                .putExtra("id", BookId).putExtra("cash", false))
        }

        db = SqlDatabase(this)
        list_payments = findViewById<RecyclerView>(R.id.list_payments)
        totalBooks = findViewById<TextView>(R.id.totalBooks)
        val layoutManager = GridLayoutManager(this, 1)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        list_payments.isNestedScrollingEnabled = false
        list_payments.layoutManager = layoutManager
        getData()

        val searchTxt = findViewById<EditText>(R.id.search_text)
        searchTxt.doOnTextChanged { text, start, before, count ->
            filter(searchTxt.text.toString())
        }
    }

    var list = ArrayList<DataModel>()
    lateinit var adapter: DataAdapter

    fun getData() {
        list = db.getData(BookId)
        adapter = DataAdapter(list)
        totalBooks.text = "Showing " + list.size + " entries"
        list_payments.adapter = adapter
        txtIn.text = "" + AmountCalculator.getBookIn(this, BookId)
        txtOut.text = "" + AmountCalculator.getBookOut(this, BookId)
        txtTotal.text = "" + (AmountCalculator.getBookIn(this, BookId) - AmountCalculator.getBookOut(this, BookId))

    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun filter(text: String) {
        val filteredlist: ArrayList<DataModel> = ArrayList()
        for (item in list) {
            if (item.partyName.toLowerCase().contains(text.toLowerCase()) || item.amount.toLowerCase().contains(text.toLowerCase()) || item.remark.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            notFound.visibility = View.VISIBLE
            list_payments.visibility = View.GONE
        } else {
            adapter.filterList(filteredlist)
            notFound.visibility = View.GONE
            list_payments.visibility = View.VISIBLE
        }
    }
}