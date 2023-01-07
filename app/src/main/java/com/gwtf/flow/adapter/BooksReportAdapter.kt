package com.gwtf.flow.adapter

import android.content.Intent
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.github.marlonlom.utilities.timeago.TimeAgoMessages
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gwtf.flow.BookActivity
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.DuplicateBookActivity
import com.gwtf.flow.EditBookNameActivity
import com.gwtf.flow.R
import com.gwtf.flow.Utilites.AmountCalculator
import com.gwtf.flow.Utilites.Constants.Business_Selected
import com.gwtf.flow.Utilites.IDGenrator
import com.gwtf.flow.Utilites.getDateTime
import com.gwtf.flow.model.BookModel
import com.gwtf.flow.model.BusinessModel
import com.gwtf.flow.model.DataModel
import org.checkerframework.common.subtyping.qual.Bottom
import java.sql.Time
import java.util.*
import java.util.stream.LongStream

class BooksReportAdapter (private var LST: List<DataModel>):
 RecyclerView.Adapter<BooksReportAdapter.ViewHolder>() {

     class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
         fun bind (list: DataModel) {
             val totalOut = itemView.findViewById<TextView>(R.id.totalOut)
             val totalIn = itemView.findViewById<TextView>(R.id.totalIn)
             val nameTxt = itemView.findViewById<TextView>(R.id.nameTxt)
             val dateTxt = itemView.findViewById<TextView>(R.id.dateTxt)

             if (list.paymentType.equals("OUT"))
                 totalOut.text = list.amount
             else
                 totalIn.text = list.amount

             Log.d("ee","Text " + list.paymentType)

             nameTxt.text = list.partyName
             dateTxt.text = list.date + " "+ list.time
         }

     }

    fun filterList(filterlist: ArrayList<DataModel>) {
        LST = filterlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_report_all, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolder).bind(LST[position])
    }

    override fun getItemCount(): Int {
        return LST.size
    }

}

