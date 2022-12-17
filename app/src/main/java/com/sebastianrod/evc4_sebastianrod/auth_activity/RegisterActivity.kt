package com.sebastianrod.evc4_sebastianrod.auth_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.sebastianrod.evc4_sebastianrod.R
import com.sebastianrod.evc4_sebastianrod.databinding.ActivityLoginBinding
import com.sebastianrod.evc4_sebastianrod.databinding.ActivityRegisterBinding
import com.sebastianrod.evc4_sebastianrod.utils.ForValidations

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        initialHiddenMessagesError()

        binding.registerLinkLogin.setOnClickListener{
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }

        binding.registerSignupButton.setOnClickListener {

            if (checkInputsCompleted()){
                val email = ForValidations.removeBlanks(binding.registerEmail.text.toString())
                val password = ForValidations.removeBlanks(binding.registerPassword.text.toString())
                binding.registerSignupButton.isEnabled = false
                signUp(email, password)
            }
        }

        setContentView(binding.root)
    }

    private fun checkInputsCompleted():Boolean{
        val checks = arrayOf(
            ForValidations.valInput(binding.registerEmail, binding.registerEmailError, ForValidations::valOnlyEmail),
            ForValidations.valInput(binding.registerPassword, binding.registerPasswordError, ForValidations::valOnlyPassword)
        )
        return !checks.contains(true)
    }

    private fun signUp(email:String, password:String){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if (it.isSuccessful) {
                    Toast.makeText(applicationContext, "Account created successfully ✅", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                } else {
                    Toast.makeText(applicationContext, "Account created failed ❌", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun initialHiddenMessagesError(){
        binding.registerEmailError.isVisible = false
        binding.registerPasswordError.isVisible = false
    }
}