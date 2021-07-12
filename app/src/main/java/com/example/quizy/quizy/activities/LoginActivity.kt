package com.example.quizy.quizy.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.quizy.R
import com.example.quizy.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var googleSignInClient : GoogleSignInClient

    private companion object{
        private const val RC_SIGN_IN = 100
    }

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        googleSignIn()

        binding.btnLogIn.setOnClickListener {
            logIn()
        }
        binding.btnGoogle.setOnClickListener {
            signIn()
        }
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this , SignupActivity ::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun googleSignIn(){
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun logIn(){
        val email = findViewById<EditText>(R.id.etEmailAddress).text.toString()
        val password = findViewById<EditText>(R.id.etPassword).text.toString()


        if(email.isBlank() || password.isBlank()){
            Toast.makeText(this , "Email and Password can't be blank" , Toast.LENGTH_SHORT).show()
            return

        }

        firebaseAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(this){
            if (it.isSuccessful){

                Toast.makeText(this , "Success" , Toast.LENGTH_SHORT).show()
                val intent = Intent(this , MainActivity ::class.java)
                startActivity(intent)
                finish()

            }else{

                Toast.makeText(this , "Authentication Failed" , Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
               // Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(account:GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this, "Successfully Logged In" , Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "signInWithCredential:success")
                    val intent = Intent(this , MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Failed To LogIn" , Toast.LENGTH_SHORT).show()
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                }
            }
    }
}