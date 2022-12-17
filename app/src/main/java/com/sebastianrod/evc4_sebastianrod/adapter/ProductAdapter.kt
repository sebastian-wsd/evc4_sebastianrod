package com.sebastianrod.evc4_sebastianrod.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sebastianrod.evc4_sebastianrod.R
import com.sebastianrod.evc4_sebastianrod.model.Product

class ProductAdapter (private val productList:List<Product>):RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(layoutInflater.inflate(R.layout.item_product, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = productList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = productList.size


}