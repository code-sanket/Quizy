package com.example.quizy.quizy.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.quizy.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser?.displayName != null){
            binding.txtEmail.text = firebaseAuth.currentUser?.displayName
        }else{
            binding.txtEmail.text = firebaseAuth.currentUser?.email
        }

        if(firebaseAuth.currentUser?.photoUrl != null){
            val uid = firebaseAuth.currentUser?.photoUrl
            Glide.with(this).load(uid).into(binding.propic)
        }




        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this , LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}