package com.example.myapplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    var selectedBtn = 0
    val userName = "JB"

    var age:Int = 0
    var category = emptyList<Int>()
    var with = ""

    var cityName = ""
    var planList = mutableStateListOf("도쿄")

    var memoryList = mutableStateListOf<String>()

    var memoryTitle = "viewTitle"
    var memoryContent = "viewContent"
    var memoryImageUrl:String? = null
}