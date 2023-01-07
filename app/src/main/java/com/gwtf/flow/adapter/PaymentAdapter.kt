package com.gwtf.flow.adapter

import android.content.Intent
import android.provider.MediaStore.Audio.Radio
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

class PaymentAdapter (private val LST: List<PaymentModel>):
 RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {

     class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
         fun bind (list: PaymentModel) {

             val radio = itemView.findViewById<RadioButton>(R.id.radio)
             val name = itemView.findViewById<TextView>(R.id.name)

             radio.isChecked = ChooseActivityActivity.categoy.equals(list.name)

             val db = SqlDatabase(itemView.context)
             /// image
             name.text = list.name

             itemView.setOnClickListener {
                 ChooseActivityActivity.categoy = list.name
                 if (ChooseActivityActivity.categoy.equals(list.name)) {
                     ChooseActivityActivity.isSelected = true
                     radio.isChecked = true
                 } else {
                     ChooseActivityActivity.isSelected = false
                     radio.isChecked = false
                 }
                 ChooseActivityActivity.getData()
             }
         }
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_categories, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolder).bind(LST[position])
    }

    override fun getItemCount(): Int {
        return LST.size
    }

}

