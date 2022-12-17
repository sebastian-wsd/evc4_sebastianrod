package com.sebastianrod.evc4_sebastianrod.auth_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.sebastianrod.evc4_sebastianrod.MainActivity
import com.sebastianrod.evc4_sebastianrod.R
import com.sebastianrod.evc4_sebastianrod.databinding.ActivityLoginBinding
import com.sebastianrod.evc4_sebastianrod.databinding.ActivitySplashscreenBinding
import com.sebastianrod.evc4_sebastianrod.utils.ForValidations

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        initialHiddenMessagesError()

        binding.loginLinkRegister.setOnClickListener{
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }

        binding.loginSigninButton.setOnClickListener {

            if (checkInputsCompleted()){
                val email = ForValidations.removeBlanks(binding.loginEmail.text.toString())
                val password = ForValidations.removeBlanks(binding.loginPassword.text.toString())
                binding.loginSigninButton.isEnabled = false
                signIn(email, password)
            }
        }

        setContentView(binding.root)
    }

    private fun signIn(email:String, password:String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(applicationContext, "Sign in successful ✅", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                } else {
                    Toast.makeText(applicationContext, "Sign in failed ❌", Toast.LENGTH_SHORT).show()
                    binding.loginSigninButton.isEnabled = true
                }
            }
    }


    private fun checkInputsCompleted():Boolean{
        val checks = arrayOf(
            ForValidations.valInput(binding.loginEmail, binding.loginEmailError, ForValidations::valOnlyEmail),
            ForValidations.valInput(binding.loginPassword, binding.loginPasswordError, ForValidations::valOnlyPassword)
        )
        return !checks.contains(true)
    }


    private fun initialHiddenMessagesError(){
        binding.loginEmailError.isVisible = false
        binding.loginPasswordError.isVisible = false
    }
}