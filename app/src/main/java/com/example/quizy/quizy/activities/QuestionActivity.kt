package com.example.quizy.quizy.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizy.R
import com.example.quizy.databinding.ActivityQuestionBinding
import com.example.quizy.quizy.adapters.Options_Adapter
import com.example.quizy.quizy.models.Questions
import com.example.quizy.quizy.models.Quiz
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class QuestionActivity : AppCompatActivity() {

    lateinit var binding: ActivityQuestionBinding

    var quizzes : MutableList<Quiz>? = null
    var questions:MutableMap<String , Questions>? = null
    var index = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpFirestore()
        setUpEventListner()

    }

    private fun setUpEventListner() {
        binding.btnPrevious.setOnClickListener {
            index--
            bindViews()
        }

        binding.btnNext.setOnClickListener {
            index++
            bindViews()
        }

        binding.btnSubmit.setOnClickListener {
            Log.d("FINALQUIZ" , questions.toString())

            val intent = Intent(this , ResultActivity::class.java)
            val json = Gson().toJson(quizzes!![0])
            intent.putExtra("QUIZ" , json)
            startActivity(intent)

        }
    }

    private fun setUpFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        var date = intent.getStringExtra("DATE")
        if(date != null){

            firestore.collection("quizzes").whereEqualTo("title" , date)
                .get()
                .addOnSuccessListener {
                    if (it != null && !it.isEmpty){
                        quizzes = it.toObjects(Quiz::class.java)
                        questions = quizzes!![0].questions
                        bindViews()
                    }

                }
        }

    }

    private fun bindViews() {
        binding.btnPrevious.visibility = View.GONE
        binding.btnNext.visibility= View.GONE
        binding.btnSubmit.visibility= View.GONE

        if (index == 1){
            // first question
            binding.btnNext.visibility = View.VISIBLE

        }else if (index == questions!!.size){
            // last question
            binding.btnSubmit.visibility = View.VISIBLE
            binding.btnPrevious.visibility = View.VISIBLE

        }else{
            // in between questions
            binding.btnNext.visibility = View.VISIBLE
            binding.btnPrevious.visibility = View.VISIBLE

        }
        val questions = questions!!["question$index"]
        questions?.let {
            binding.description.text = it.description
            val optionAdapter = Options_Adapter(this , it)
            binding.optionList.layoutManager = LinearLayoutManager(this)
            binding.optionList.adapter = optionAdapter
            binding.optionList.setHasFixedSize(true)
        }

    }
}