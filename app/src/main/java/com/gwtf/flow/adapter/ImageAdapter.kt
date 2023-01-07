package com.gwtf.flow.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gwtf.flow.ProductViewActivity
import com.gwtf.flow.R
import com.gwtf.flow.model.CardModel
import com.gwtf.flow.model.ImageModel

class ImageAdapter (private var LST: List<ImageModel>):
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

        class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            fun bind(list: ImageModel) {
                val image = itemView.findViewById<ImageView>(R.id.image)
                Glide.with(itemView.context).load(list.Image).into(image)

                itemView.setOnClickListener {
                    Glide.with(itemView.context).load(list.Image).into(ProductViewActivity.mainImage)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, null)
        return ImageAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageAdapter.ViewHolder, position: Int) {
        (holder as ImageAdapter.ViewHolder).bind(LST[position])
    }

    override fun getItemCount(): Int {
        return LST.size
    }
}