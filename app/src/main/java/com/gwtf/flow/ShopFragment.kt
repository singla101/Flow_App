package com.gwtf.flow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.api.Distribution.BucketOptions.Linear
import com.google.firebase.firestore.FirebaseFirestore
import com.gwtf.flow.R
import com.gwtf.flow.adapter.DomainAdapter
import com.gwtf.flow.adapter.ProductAdapter
import com.gwtf.flow.model.DomainModel
import com.gwtf.flow.model.ProductModel

class ShopFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_shop, container, false)

        val listDomain = view.findViewById<RecyclerView>(R.id.listDomain)
        val layoutManager = LinearLayoutManager(view.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        listDomain.layoutManager = layoutManager

        val list = ArrayList<DomainModel>()
        list.add(DomainModel("guru_store.com", "48", "299", "1199"))
        list.add(DomainModel("shopsolutions.co", "79", "499", "299"))
        list.add(DomainModel("mart.farm", "89", "699", "2799"))
        list.add(DomainModel("mart.rocks", "68", "399", "2299"))
        val adapter = DomainAdapter(list)
        listDomain.adapter = adapter

//        val addProductBtn = view.findViewById<LinearLayout>(R.id.addProductBtn)
//        addProductBtn.setOnClickListener {
//            startActivity(Intent(view.context, AddProductActivity::class.java))
//        }

        val addProductBtna = view.findViewById<LinearLayout>(R.id.addProductBtna)
        addProductBtna.setOnClickListener {
            startActivity(Intent(view.context, AddProductActivity::class.java))
        }

        val addButton = view.findViewById<TextView>(R.id.addButton)
        addButton.setOnClickListener {
            startActivity(Intent(view.context, AddProductActivity::class.java))
        }

        val manageInvantory = view.findViewById<CardView>(R.id.manageInvantory)
        manageInvantory.setOnClickListener {
            startActivity(Intent(view.context, InvantoryActivity::class.java))
        }

        val websiteSettings = view.findViewById<CardView>(R.id.websiteSettings)
        websiteSettings.setOnClickListener {
            startActivity(Intent(view.context, WebsiteActivity::class.java))
        }

        val listData = view.findViewById<RecyclerView>(R.id.listData)
        val listlayoutManager = GridLayoutManager(view.context, 1)
        listlayoutManager.orientation = LinearLayoutManager.VERTICAL
        listData.layoutManager = listlayoutManager

        var lisst = ArrayList<ProductModel>()

        val db = FirebaseFirestore.getInstance()
        db.collection("PRODUCTS").get().addOnSuccessListener { result ->
            for (doc in result) {
                lisst.add(
                    ProductModel(doc.id, doc.get("TITLE").toString(), doc.get("Image1").toString(),
                        doc.get("PRICE").toString(), doc.get("DESCRIPTION").toString(), doc.get("BUSINESSID").toString(),
                        doc.get("CATEGORY").toString(), doc.getBoolean("InStock") == true)
                )
            }
            val adapter = ProductAdapter(lisst)
            listData.adapter = adapter
        }


        return view
    }
}