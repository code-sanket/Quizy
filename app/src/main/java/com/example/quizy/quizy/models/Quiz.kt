package com.example.quizy.quizy.models

import android.icu.text.CaseMap

data class Quiz(
     var id:String = "",
     var title:String = "",
     var questions: MutableMap<String , Questions> = mutableMapOf()
 )
