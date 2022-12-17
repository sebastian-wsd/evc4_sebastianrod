package com.sebastianrod.evc4_sebastianrod.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sebastianrod.evc4_sebastianrod.R
import com.sebastianrod.evc4_sebastianrod.model.Product
import com.squareup.picasso.Picasso

class ProductViewHolder(val view:View) :RecyclerView.ViewHolder(view){

    val pr_code = view.findViewById<TextView>(R.id.item_product_code)
    val pr_description = view.findViewById<TextView>(R.id.item_product_description)
    val pr_brand = view.findViewById<TextView>(R.id.item_product_brand)
    val pr_stock = view.findViewById<TextView>(R.id.item_product_stock)
    val pr_purchase_price = view.findViewById<TextView>(R.id.item_product_purchase_price)
    val pr_sale_price = view.findViewById<TextView>(R.id.item_product_sale_price)
    val pr_image = view.findViewById<ImageView>(R.id.item_product_image)

    fun render(product:Product){
        pr_code.text = product.code.toString()
        pr_description.text = product.description
        pr_brand.text = product.brand
        pr_stock.text = product.stock.toString()
        pr_purchase_price.text = "$ %.2f".format(product.purchasePrice)
        pr_sale_price.text = "$ %.2f".format(product.salePrice)
//        Glide.with(view).load(product.image).into(pr_image)
        Picasso.get().load(product.image).into(pr_image)

    }

}