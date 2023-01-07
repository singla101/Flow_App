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
import com.gwtf.flow.*
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.Utilites.AmountCalculator
import com.gwtf.flow.Utilites.Constants.Business_Selected
import com.gwtf.flow.Utilites.IDGenrator
import com.gwtf.flow.Utilites.getDateTime
import com.gwtf.flow.model.*
import org.checkerframework.common.subtyping.qual.Bottom
import java.sql.Time
import java.util.*
import java.util.stream.LongStream

class ContactAdapter (private var LST: List<ContactModel>):
 RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

     class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
         fun bind (list: ContactModel) {
             val txt_name = itemView.findViewById<TextView>(R.id.txt_name)
             val txt_flatter = itemView.findViewById<TextView>(R.id.txt_flatter)
             val txt_mobile = itemView.findViewById<TextView>(R.id.txt_mobile)

             val db = SqlDatabase(itemView.context)
             /// image
             txt_name.text = list.name
             txt_flatter.text = list.name.get(0).toString()
             txt_mobile.text = "+91 " + list.number

             itemView.setOnClickListener {
                 ChoosePartyActivity.personName = list.name
                 ChoosePartyActivity.mobileNumber = list.number
                 ChoosePartyActivity.addParty()
             }
         }
     }

    fun filterList(filterlist: ArrayList<ContactModel>) {
        LST = filterlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolder).bind(LST[position])
    }

    override fun getItemCount(): Int {
        return LST.size
    }

}

