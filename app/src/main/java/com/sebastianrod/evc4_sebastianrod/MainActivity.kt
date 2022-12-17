package com.sebastianrod.evc4_sebastianrod

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sebastianrod.evc4_sebastianrod.adapter.ProductAdapter
import com.sebastianrod.evc4_sebastianrod.auth_activity.LoginActivity
import com.sebastianrod.evc4_sebastianrod.databinding.ActivityMainBinding
import com.sebastianrod.evc4_sebastianrod.model.Product
import com.sebastianrod.evc4_sebastianrod.model.custom.ArrayForClasses

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var productList = mutableListOf<Product>()
    private val databaseFirestore= FirebaseFirestore.getInstance()
    private var loadedProducts = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        binding.productRecyclerview.layoutManager = LinearLayoutManager(applicationContext)
        initialHiddenAndEnabledMessagesError()

        binding.productRegisterButton.setOnClickListener {
            if (loadedProducts){
                startActivity(Intent(applicationContext, RegisterProductActivity::class.java))
            } else {
                Toast.makeText(applicationContext, "Products have not been loaded yet 🕑", Toast.LENGTH_SHORT).show()
            }
        }

        binding.productSearchButton.setOnClickListener {
            println("Activando búsqueda")
            if (loadedProducts){
                searchProductByCode()
            } else {
                Toast.makeText(applicationContext, "Products have not been loaded yet 🕑", Toast.LENGTH_SHORT).show()
            }
        }

        binding.productReload.setOnClickListener{
            binding.productVoidMessage.isVisible = false
            binding.productReload.isVisible = false
            getProducts()
        }

        binding.logoutButton.setOnClickListener {
            signOut()
        }

        getProducts()
        setContentView(binding.root)
    }

    private fun getProducts(){
        productList = mutableListOf()
        databaseFirestore.collection("product").get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val code = document.data["code"].toString().toInt()
                    val description = document.data["description"].toString()
                    val brand = document.data["brand"].toString()
                    val purchasePrice = document.data["purchasePrice"].toString().toDouble()
                    val salePrice = document.data["salePrice"].toString().toDouble()
                    val stock = document.data["stock"].toString().toInt()
                    val image = document.data["image"].toString()
                    val product = Product(code, description, brand, purchasePrice, salePrice, stock, image)
                    productList.add(product)
                }
                binding.productRecyclerview.adapter = ProductAdapter(productList)
                ArrayForClasses.productList = productList
                println(productList)
                loadedProducts = true
                binding.productRegisterButton.isEnabled = true
                binding.productSearchButton.isEnabled = true
            }

    }

    private fun searchProductByCode(){
        val codeInput = binding.productSearchCode.text
        if (codeInput.isNotEmpty()){
            val tempProductList = mutableListOf<Product>()
            println("Código de producto buscado -> ${codeInput}")
            databaseFirestore.collection("product")
                .whereEqualTo("code", codeInput.toString().toInt())
                .get()
                .addOnSuccessListener {
                    result ->
                    for (document in result){
                        val code = document.data["code"].toString().toInt()
                        val description = document.data["description"].toString()
                        val brand = document.data["brand"].toString()
                        val purchasePrice = document.data["purchasePrice"].toString().toDouble()
                        val salePrice = document.data["salePrice"].toString().toDouble()
                        val stock = document.data["stock"].toString().toInt()
                        val image = document.data["image"].toString()
                        val product = Product(code, description, brand, purchasePrice, salePrice, stock, image)
                        println("Producto encontrado -> $product")
                        tempProductList.add(product)
                    }
                    if (tempProductList.isEmpty()){
                        binding.productVoidMessage.isVisible = true
                        binding.productReload.isVisible = true
                        binding.productRecyclerview.isVisible = false
                    } else {
                        binding.productVoidMessage.isVisible = false
                        binding.productRecyclerview.isVisible = true
                        binding.productReload.isVisible = true
                        binding.productRecyclerview.adapter = ProductAdapter(tempProductList)
                    }
                }
        }
    }

    private fun signOut(){
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(applicationContext, LoginActivity::class.java))
    }

    private fun initialHiddenAndEnabledMessagesError(){
        binding.productRegisterButton.isEnabled = false
        binding.productSearchButton.isEnabled = false
        binding.productVoidMessage.isVisible = false
        binding.productReload.isVisible = false
    }



}