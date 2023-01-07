package com.gwtf.flow.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.parser.ColorParser
import com.gwtf.flow.CashActivity
import com.gwtf.flow.PaymentModeActivity
import com.gwtf.flow.R
import com.gwtf.flow.model.SingleModel

class SingleAdapter (val LST: List<SingleModel>): RecyclerView.Adapter<SingleAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(list: SingleModel) {
            val textView = itemView.findViewById<TextView>(R.id.txt_tag)

            if (CashActivity.paymentMode.equals(list.text)) {
                textView.setTextColor(Color.parseColor("#5dc7d6"))
            }

            textView.text = list.text
            itemView.setOnClickListener {
                CashActivity.paymentMode = list.text
                textView.setTextColor(Color.parseColor("#5dc7d6"))
                CashActivity.updatePayment()
            }

            if (textView.text.toString().equals("Add New")) {
                itemView.setOnClickListener {
                    itemView.context.startActivity(Intent(itemView.context, PaymentModeActivity::class.java))
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tags, null)
        return SingleAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SingleAdapter.ViewHolder, position: Int) {
        (holder as SingleAdapter.ViewHolder).bind(LST[position])
    }

    override fun getItemCount(): Int {
        return LST.size
    }


}