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
import com.gwtf.flow.model.DomainModel
import org.checkerframework.common.subtyping.qual.Bottom
import java.sql.Time
import java.util.*
import java.util.stream.LongStream

class DomainAdapter (private var LST: List<DomainModel>):
 RecyclerView.Adapter<DomainAdapter.ViewHolder>() {

     class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
         fun bind (list: DomainModel) {
             val domain = itemView.findViewById<TextView>(R.id.domain)
             val discount = itemView.findViewById<TextView>(R.id.discount)
             val price = itemView.findViewById<TextView>(R.id.price)
             val mrp = itemView.findViewById<TextView>(R.id.mrp)

             domain.text = list.domain
             discount.text = "Flat" + list.discount + "% off"
             price.text = "Rs. " + list.price
             mrp.text = list.mrp
         }
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_domain, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolder).bind(LST[position])
    }

    override fun getItemCount(): Int {
        return LST.size
    }

}

