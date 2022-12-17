package com.sebastianrod.evc4_sebastianrod

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.sebastianrod.evc4_sebastianrod.auth_activity.LoginActivity
import com.sebastianrod.evc4_sebastianrod.databinding.ActivitySplashscreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        binding.splashscreenContainer.isVisible = false

        var timerShowActivity = GlobalScope.launch {  }

        timerShowActivity = GlobalScope.launch(Dispatchers.Main) {
            delay(3000)
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }
        animateContainer()

    }
    private fun animateContainer(){
        var timerShowContainer = GlobalScope.launch {  }

        timerShowContainer = GlobalScope.launch(Dispatchers.Main) {
            delay(500)
            binding.splashscreenContainer.isVisible = true
            val objectAnimator = ObjectAnimator.ofFloat(binding.splashscreenContainer, View.ALPHA, 0.0f, 1.0f)
            objectAnimator.setDuration(500)
            val animatorSet = AnimatorSet()
            animatorSet.play(objectAnimator)
            animatorSet.start()
        }

    }

}