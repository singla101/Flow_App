package com.gwtf.flow.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.HomeFragment
import com.gwtf.flow.MainActivity
import com.gwtf.flow.R
import com.gwtf.flow.Utilites.Constants
import com.gwtf.flow.Utilites.Constants.Business_Selected
import com.gwtf.flow.model.BusinessModel

class BusinessAdapter (private val LST: List<BusinessModel>):
 RecyclerView.Adapter<BusinessAdapter.ViewHolder>() {

     class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
         fun bind (list: BusinessModel) {
             val txt_disciption = itemView.findViewById<TextView>(R.id.txt_disciption)
             val txt_business_name = itemView.findViewById<TextView>(R.id.txt_business_name)
             val img_selected = itemView.findViewById<ImageView>(R.id.img_selected)
             val db = SqlDatabase(itemView.context)
             /// image
             txt_business_name.text = list.businessName
             txt_disciption.text = "Your Role: Owner . " + db.getBooks(list.id).size + " Books"

             if (Business_Selected == list.id) {
                 img_selected.visibility = View.VISIBLE
             } else {
                 img_selected.visibility = View.GONE
             }

             val imageView = itemView.findViewById<ImageView>(R.id.image)
             if(list.image.isEmpty() || list.image.equals(null)) {
                 imageView.setImageResource(R.drawable.logo)
             } else {
                 Glide.with(itemView.context).load(list.image).into(imageView)
             }

             val sharedPref = itemView.context.getSharedPreferences(itemView.context.packageName, AppCompatActivity.MODE_PRIVATE)
             val editor = sharedPref.edit()
             itemView.setOnClickListener {
                 editor.apply {
                     putString("BusinessSelected", list.id)
                     putString("BusinessName", list.businessName)
                     putString("BusinessImage", list.image)
                     apply()
                 }

                 itemView.context.startActivity(Intent(itemView.context, MainActivity::class.java))
                 (itemView.context as Activity).finish()
             }
         }
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_business, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolder).bind(LST[position])
    }

    override fun getItemCount(): Int {
        return LST.size
    }

}

