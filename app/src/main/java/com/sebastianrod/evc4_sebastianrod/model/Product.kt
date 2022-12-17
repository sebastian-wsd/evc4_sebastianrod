package com.sebastianrod.evc4_sebastianrod.model

data class Product (
    val code:Int,
    val description:String,
    val brand:String,
    val purchasePrice:Double,
    val salePrice:Double,
    val stock:Int,
    val image:String
    )