package com.gwtf.flow.adapter

import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.github.marlonlom.utilities.timeago.TimeAgoMessages
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gwtf.flow.*
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.Utilites.AmountCalculator
import com.gwtf.flow.Utilites.Constants.Business_Selected
import com.gwtf.flow.Utilites.IDGenrator
import com.gwtf.flow.Utilites.getDateTime
import com.gwtf.flow.model.BookModel
import com.gwtf.flow.model.BusinessModel
import com.gwtf.flow.model.CategoriesModel
import com.gwtf.flow.model.PartyModel
import org.checkerframework.common.subtyping.qual.Bottom
import java.sql.Time
import java.util.*
import java.util.stream.LongStream

class BusinessCategoryAdapter (private val LST: List<CategoriesModel>):
 RecyclerView.Adapter<BusinessCategoryAdapter.ViewHolder>() {

     class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
         fun bind (list: CategoriesModel) {

             val txt_name = itemView.findViewById<TextView>(R.id.txt_name)

             val imageView = itemView.findViewById<ImageView>(R.id.imageView)
             val imgSelected = itemView.findViewById<ImageView>(R.id.imgSelected)

             if(SelectBusinessCategoryActivity.type.equals(list.name)) {
                 imgSelected.visibility = View.VISIBLE
             } else {
                 imgSelected.visibility = View.GONE
             }

             Glide.with(itemView.context).load(list.id).into(imageView)
             val db = SqlDatabase(itemView.context)
             /// image
             txt_name.text = list.name

             itemView.setOnClickListener {
                 SelectBusinessCategoryActivity.type = list.name
                 if (SelectBusinessCategoryActivity.type.equals(list.name)) {
                     SelectBusinessCategoryActivity.isSelected = true
                     imgSelected.visibility = View.VISIBLE
                 } else {
                     SelectBusinessCategoryActivity.isSelected = false
                     imgSelected.visibility = View.GONE
                 }
                 SelectBusinessCategoryActivity.getData()
             }
         }
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolder).bind(LST[position])
    }

    override fun getItemCount(): Int {
        return LST.size
    }

}

