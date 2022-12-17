package com.sebastianrod.evc4_sebastianrod

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.sebastianrod.evc4_sebastianrod.databinding.ActivityRegisterProductBinding
import com.sebastianrod.evc4_sebastianrod.model.Product
import com.sebastianrod.evc4_sebastianrod.model.custom.ArrayForClasses
import com.sebastianrod.evc4_sebastianrod.utils.ForValidations

class RegisterProductActivity : AppCompatActivity() {

    private lateinit var binding:ActivityRegisterProductBinding
    private lateinit var productCollection:CollectionReference
    private val databaseFirestore= FirebaseFirestore.getInstance()
    private val REQUEST_GALLERY = 101
    private lateinit var filename:String
    private var imageUri:Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterProductBinding.inflate(layoutInflater)
        initialHiddenMessagesError()
        createCodeProduct()

        binding.productRegisterButtonCancel.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        binding.productRegisterButtonOpenGallery.setOnClickListener {
            openGalleryWithPermissions()
        }

        binding.registerBack.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        binding.productRegisterButtonSave.setOnClickListener {
            if (checkInputsCompleted()){
                println("Send product...")
                binding.productRegisterButtonSave.isEnabled = false
                binding.productRegisterButtonCancel.isEnabled = false
                binding.registerBack.isEnabled = false
                binding.productRegisterButtonOpenGallery.isEnabled = false
                registerProductWithImage()
            }
        }


        setContentView(binding.root)
    }

    private fun registerProduct(image:String){
        val product = Product(createCodeProduct(), binding.registerDescription.text.toString(),
            binding.registerBrand.text.toString(), binding.registerPurchasePrice.text.toString().toDouble(),
            binding.registerSalePrice.text.toString().toDouble(), binding.registerStock.text.toString().toInt(),
            image)
        productCollection = databaseFirestore.collection("product")
        productCollection.add(product).addOnSuccessListener {
            Toast.makeText(applicationContext, "Product created successfully ✅", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }.addOnFailureListener {exception ->
            Toast.makeText(applicationContext, "Product created failure: $exception", Toast.LENGTH_SHORT).show()
        }

    }

    private fun registerProductWithImage(){
        val storageReference = FirebaseStorage.getInstance().getReference("images/$filename")
        storageReference.putFile(imageUri!!).addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener {
                registerProduct(it.toString())
            }
        }

    }

    private fun createCodeProduct():Int{
        var code:Int
        if (ArrayForClasses.productList.isEmpty()){
            code = 1000
        } else {
            code = 0
            for (product in  ArrayForClasses.productList){
                if (product.code > code){
                    code = product.code
                }
            }
            code += 1
        }
        println(ArrayForClasses.productList)
        return code
    }

    private fun checkInputsCompleted():Boolean{
        val checks = arrayOf(
            ForValidations.valInput(binding.registerDescription, binding.registerDescriptionError, ForValidations::valAllTypeText),
            ForValidations.valInput(binding.registerBrand, binding.registerBrandError, ForValidations::valAllTypeText),
            ForValidations.valInput(binding.registerPurchasePrice, binding.registerPurchasePriceError, ForValidations::valDecimal),
            ForValidations.valInput(binding.registerSalePrice, binding.registerSalePriceError, ForValidations::valDecimal),
            ForValidations.valInput(binding.registerStock, binding.registerStockError, ForValidations::valNumber),
            validateImage(imageUri, binding.registerImageError)
        )
        return !checks.contains(true)
    }

    private fun validateImage(uri:Uri?, textViewError: TextView):Boolean{
        val existError = uri == null
        textViewError.isVisible = existError
        return existError
    }

    private fun initialHiddenMessagesError(){
        binding.registerDescriptionError.isVisible = false
        binding.registerBrandError.isVisible = false
        binding.registerPurchasePriceError.isVisible = false
        binding.registerSalePriceError.isVisible = false
        binding.registerStockError.isVisible = false
        binding.registerImageError.isVisible = false
    }

    private fun openGalleryWithPermissions(){
        Toast.makeText(applicationContext, "Open gallery...", Toast.LENGTH_SHORT).show()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                // Pedimos los permisos
                val permissionFile = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissionFile, REQUEST_GALLERY)
            } else {
                onlyOpenGallery()
            }
        } else{
            onlyOpenGallery()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_GALLERY -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) onlyOpenGallery()
                else Toast.makeText(applicationContext, "No access to the gallery", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onlyOpenGallery(){
        val intentGallery = Intent(Intent.ACTION_PICK)
        intentGallery.type = "image/*"
        startActivityForResult(intentGallery, REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY) {
            imageUri = data?.data!!
            filename = imageUri?.lastPathSegment.toString()
            binding.registerImage.setImageURI(imageUri)
        }
    }




}