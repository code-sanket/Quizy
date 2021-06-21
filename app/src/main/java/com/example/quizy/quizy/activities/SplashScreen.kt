package com.example.quizy.quizy.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.quizy.databinding.ActivityLoginBinding
import kotlin.concurrent.timer

class SplashScreen : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val time: Long = 1000

        Handler().postDelayed(Runnable {
            val intent = Intent(SplashScreen@this , LoginIntro::class.java )
            startActivity(intent)
            finish()
        } ,time )



    }

}