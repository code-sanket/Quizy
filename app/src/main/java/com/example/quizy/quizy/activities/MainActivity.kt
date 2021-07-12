package com.example.quizy.quizy.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quizy.R
import com.example.quizy.databinding.ActivityMainBinding
import com.example.quizy.quizy.adapters.QuizAdapter
import com.example.quizy.quizy.models.Quiz
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var adapter: QuizAdapter
    lateinit var firestore: FirebaseFirestore

    private var quizList = mutableListOf<Quiz>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViews()

    }

    fun setUpViews() {
        setDrawerLayout()
        setUpAdapter()
        setUpFireStore()
        setUpDatePicker()
    }

    private fun setUpDatePicker() {
        binding.btnDataPicker.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener {

                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                val date = dateFormatter.format(Date(it))

                // checking if the question for the date is available or not //
                // @improvised by sanky //

                val firestore = FirebaseFirestore.getInstance()
                firestore.collection("quizzes").whereEqualTo("title" , date)
                    .get()
                    .addOnSuccessListener {
                        if (it != null && !it.isEmpty){
                            val intent = Intent(this , QuestionActivity::class.java)
                            intent.putExtra("DATE" , date)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this , "Questions Not Available yet" , Toast.LENGTH_SHORT).show()
                        }

                    }


            }
            datePicker.addOnNegativeButtonClickListener {

            }
            datePicker.addOnCancelListener {

            }
        }
    }

    private fun setUpFireStore() {
        firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("quizzes")
        collectionReference.addSnapshotListener { value, error ->
            if (value == null || error != null){
                Toast.makeText(this , "Error while fetching Data" , Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            quizList.clear()
            quizList.addAll(value.toObjects(Quiz::class.java))
            adapter.notifyDataSetChanged()
        }
    }

    private fun setUpAdapter() {
        adapter = QuizAdapter(this, quizList)
        binding.quizRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.quizRecyclerView.adapter = adapter
    }

    fun setDrawerLayout() {
        setSupportActionBar(binding.appBar) // iska mtlb ha hum bta rhe ha ki default toolbar k jagh mera bnaya tool bar use krna ha //

        actionBarDrawerToggle =
            ActionBarDrawerToggle(this , binding.mainDrawer , R.string.app_name , R.string.app_name)
        actionBarDrawerToggle.syncState()
        binding.navigationView.setNavigationItemSelectedListener {


            //@improvised By Sanky //

            when(it.itemId){
               R.id.btnProfile ->{
                   val intent = Intent(this , ProfileActivity::class.java)
                   startActivity(intent)
                   finish()

               }

                R.id.btnFollow ->{
                    gotoMyProfile("https://www.linkedin.com/in/sanket-sinha-9a4a371a8/")
                }


            }

                binding.mainDrawer.closeDrawers()
                true
        }

    }

    // @improvised by Sanky //

    private fun gotoMyProfile(s: String) {
        val uri = Uri.parse(s)
        startActivity(Intent(Intent.ACTION_VIEW,uri))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}