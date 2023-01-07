package com.gwtf.flow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.Utilites.AmountCalculator
import com.gwtf.flow.Utilites.Constants.Business_Selected
import com.gwtf.flow.adapter.BooksReportAdapter
import com.gwtf.flow.model.DataModel

class TransactionsFragment : Fragment() {

    var list = ArrayList<DataModel>()
    lateinit var adapter: BooksReportAdapter
    lateinit var notFound: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_transactions, container, false)
        notFound = view.findViewById(R.id.notFound)
        val db = SqlDatabase(view.context)

        notFound.visibility = View.GONE

        val totalOut = view.findViewById<TextView>(R.id.totalOut)
        val totalEnteries = view.findViewById<TextView>(R.id.totalEnteries)
        val totalAmount = view.findViewById<TextView>(R.id.totalAmount)
        totalOut.text = "" + AmountCalculator.getOut(view.context)
        val totalIn = view.findViewById<TextView>(R.id.totalIn)
        totalIn.text = "" + AmountCalculator.getIn(view.context)

        totalAmount.text = "" + (AmountCalculator.getIn(view.context) - AmountCalculator.getOut(view.context))

        val listReports = view.findViewById<RecyclerView>(R.id.listReports)
        listReports.isNestedScrollingEnabled = false
        val layoutManager = GridLayoutManager(view.context, 1)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        listReports.layoutManager = layoutManager

        list = db.getBookData(Business_Selected)
        adapter = BooksReportAdapter(list)
        listReports.adapter = adapter

        totalEnteries.text = "" + list.size + " Entries"

        val searchTxt = view.findViewById<EditText>(R.id.search_text)
        searchTxt.doOnTextChanged { text, start, before, count ->
            filter(searchTxt.text.toString())
        }


        return view
    }

    private fun filter(text: String) {
        val filteredlist: ArrayList<DataModel> = ArrayList()
        for (item in list) {
            if (item.partyName.toLowerCase().contains(text.toLowerCase())) {
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