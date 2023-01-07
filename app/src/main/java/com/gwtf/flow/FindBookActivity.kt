package com.gwtf.flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.api.Distribution.BucketOptions.Linear
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.Utilites.Constants.Business_Selected
import com.gwtf.flow.adapter.BooksAdapter
import com.gwtf.flow.adapter.BooksReportAdapter
import com.gwtf.flow.adapter.BooksSearchAdapter
import com.gwtf.flow.model.BookModel
import com.gwtf.flow.model.DataModel

class FindBookActivity : AppCompatActivity() {

    var list = ArrayList<BookModel>()
    lateinit var adapter: BooksSearchAdapter
    lateinit var boooks: RecyclerView
    lateinit var notFound: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_book)
        val db = SqlDatabase(this)
        boooks = findViewById(R.id.boooks)
        val layManager = LinearLayoutManager(this)
        layManager.orientation = LinearLayoutManager.VERTICAL
        boooks.layoutManager = layManager

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }


        notFound = findViewById(R.id.notFound)
        notFound.visibility = View.GONE

        list = db.getBooks(Business_Selected)
        adapter = BooksSearchAdapter(list)
        boooks.adapter = adapter

        val searchTxt = findViewById<EditText>(R.id.search_text)
        searchTxt.doOnTextChanged { text, start, before, count ->
            filter(searchTxt.text.toString())
        }

    }

    private fun filter(text: String) {
        val filteredlist: ArrayList<BookModel> = ArrayList()
        for (item in list) {
            if (item.name.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            notFound.visibility = View.VISIBLE
        } else {
            adapter.filterList(filteredlist)
            notFound.visibility = View.GONE

        }
    }
}