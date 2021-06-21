package com.example.quizy.quizy.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.quizy.R
import com.example.quizy.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth


class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener{
            signUpUser()
        }

        binding.btnLogIn.setOnClickListener {
            val intent = Intent(this , LoginActivity ::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signUpUser(){

        val email = findViewById<EditText>(R.id.etEmailAddress).text.toString()
        val password = findViewById<EditText>(R.id.etPassword).text.toString()
        val confirmPassword = findViewById<EditText>(R.id.etConfirmPassword).text.toString()

        if(email.isBlank() || password.isBlank() || confirmPassword.isBlank()){
            Toast.makeText(this , "Email and Password can't be blank" , Toast.LENGTH_SHORT).show()
            return

        }

        if (password != confirmPassword){
            Toast.makeText(this , "Password and Confirm Password do not match" , Toast.LENGTH_SHORT).show()
            return

        }

        if(password.length < 8 || password.length > 16){
            Toast.makeText(this , "Password Size Must Be between 8 to 16 characters" , Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email ,password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful){

                    Toast.makeText(this , "Login Successful" , Toast.LENGTH_SHORT).show()
                    val intent = Intent(this , MainActivity ::class.java)
                    startActivity(intent)
                    finish()


                }else{
                    Toast.makeText(this , "Error While Creating the User" , Toast.LENGTH_SHORT).show()
                }
            }
    }
}