package com.example.quizy.quizy.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quizy.databinding.ActivityLoginIntroBinding
import com.google.firebase.auth.FirebaseAuth

class LoginIntro : AppCompatActivity() {
    lateinit var binding: ActivityLoginIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null){  // yha pr check hoga ki pehle se admi login ha ya ni //

            Toast.makeText(this , "User is already logged in" , Toast.LENGTH_SHORT).show()
            redirect("MAIN");
        }

        binding.btnLetsGo.setOnClickListener {
                redirect("LOGIN")
        }
    }

    private fun redirect (name : String){
        val intent = when(name){  // yha pr alag alag intent ko same ek sath use ho rha ha //
            "LOGIN" -> Intent(this , LoginActivity::class.java)
            "MAIN" -> Intent(this , MainActivity::class.java)
            else -> throw Exception("no path exists")
        }

        startActivity(intent)
        finish()
    }
}