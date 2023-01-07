package com.gwtf.flow.adapter

import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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
import com.gwtf.flow.model.ProductModel
import com.gwtf.flow.model.BusinessModel
import com.gwtf.flow.model.ImageModel
import org.checkerframework.common.subtyping.qual.Bottom
import java.sql.Time
import java.util.*
import java.util.stream.LongStream
import kotlin.collections.ArrayList

class ProductAdapter (private val LST: List<ProductModel>):
 RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

     class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
         fun bind (list: ProductModel) {
             val image = itemView.findViewById<ImageView>(R.id.image)
             val productTitle = itemView.findViewById<TextView>(R.id.productTitle)
             val priceTxt = itemView.findViewById<TextView>(R.id.priceTxt)
             Glide.with(itemView.context).load(list.image1).into(image)
             productTitle.text = list.title
             priceTxt.text = list.price
             val _list = ArrayList<ImageModel>()
             _list.add(ImageModel(list.image1))
             itemView.setOnClickListener {
                 itemView.context.startActivity(
                     Intent(itemView.context,
                         ProductViewActivity::class.java)
                         .putExtra("Id", list.id))
             }
         }

     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolder).bind(LST[position])
    }

    override fun getItemCount(): Int {
        return LST.size
    }

}


