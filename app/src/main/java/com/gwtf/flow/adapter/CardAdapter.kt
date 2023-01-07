package com.gwtf.flow.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.bumptech.glide.Glide
import com.gwtf.flow.BusinessCardActivity
import com.gwtf.flow.R
import com.gwtf.flow.model.CardModel
import org.w3c.dom.Text

class CardAdapter(private var LST: List<CardModel>):
    RecyclerView.Adapter<CardAdapter.ViewHolder>() {

        class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            fun bind(list: CardModel) {
                val image = itemView.findViewById<ImageView>(R.id.background)

                Glide.with(itemView.context).load(itemView.context.getResources()
                    .getIdentifier(list.Background, "drawable", itemView.context.getPackageName())).into(image)

                itemView.setOnClickListener {
                    Glide.with(itemView.context).load(itemView.context.getResources()
                        .getIdentifier(list.Background, "drawable", itemView.context.getPackageName())).into(BusinessCardActivity._image)

                    if (list.TextAlignment.equals("Center")) {
                        BusinessCardActivity._businessName.textAlignment = View.TEXT_ALIGNMENT_CENTER
                        BusinessCardActivity._ownerName.textAlignment = View.TEXT_ALIGNMENT_CENTER
                        BusinessCardActivity._accountType.textAlignment = View.TEXT_ALIGNMENT_CENTER
                        BusinessCardActivity._number.textAlignment = View.TEXT_ALIGNMENT_CENTER
                        BusinessCardActivity._address.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    } else if (list.TextAlignment.equals("Right")) {
                        BusinessCardActivity._businessName.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                        BusinessCardActivity._ownerName.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                        BusinessCardActivity._accountType.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                        BusinessCardActivity._number.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                        BusinessCardActivity._address.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                    } else {
                        BusinessCardActivity._businessName.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        BusinessCardActivity._ownerName.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        BusinessCardActivity._accountType.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        BusinessCardActivity._number.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        BusinessCardActivity._address.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                    }

                    BusinessCardActivity._businessName.setTextColor(Color.parseColor(list.TextColor))
                    BusinessCardActivity._ownerName.setTextColor(Color.parseColor(list.TextColor))
                    BusinessCardActivity._accountType.setTextColor(Color.parseColor(list.TextColor))
                    BusinessCardActivity._number.setTextColor(Color.parseColor(list.TextColor))
                    BusinessCardActivity._address.setTextColor(Color.parseColor(list.TextColor))
                    BusinessCardActivity.updateData()
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_businesscard, null)
        return CardAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardAdapter.ViewHolder, position: Int) {
        (holder as CardAdapter.ViewHolder).bind(LST[position])
    }

    override fun getItemCount(): Int {
        return LST.size
    }
}