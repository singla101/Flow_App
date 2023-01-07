package com.gwtf.flow.adapter

import android.content.Intent
import android.text.Layout
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

class BooksSearchAdapter (private var LST: List<BookModel>):
 RecyclerView.Adapter<BooksSearchAdapter.ViewHolder>() {

     class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
         fun bind (list: BookModel) {
             val txt_bookname = itemView.findViewById<TextView>(R.id.txt_bookname)
             val textView = itemView.findViewById<TextView>(R.id.textView)
             val txt_amountin = itemView.findViewById<TextView>(R.id.txt_amountin)
             val btn_more = itemView.findViewById<ImageView>(R.id.btn_more)
             val db = SqlDatabase(itemView.context)
             /// image
             txt_bookname.text = list.name

             txt_amountin.text = "" + AmountCalculator.getIn(itemView.context);

             val mili = list.date as Long
             val msg: TimeAgoMessages = TimeAgoMessages.Builder().withLocale(Locale.forLanguageTag("en")).build()
             textView.text = "Updated " + TimeAgo.using(mili, msg)

             itemView.setOnClickListener {
                 itemView.context.startActivity(
                     Intent(itemView.context, BookActivity::class.java).putExtra("id", list.id)
                 )
             }

             btn_more.visibility = View.GONE
         }
     }

    fun filterList(filterlist: ArrayList<BookModel>) {
        LST = filterlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolder).bind(LST[position])
    }

    override fun getItemCount(): Int {
        return LST.size
    }

}

