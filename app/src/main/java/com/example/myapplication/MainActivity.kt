package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.travelassistant.TravelPlanScreen
import com.google.android.libraries.places.api.Places

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler{
                thread, throwable -> Log.e("mymymy", thread.name + "::" + throwable.message, throwable)
        }
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, BuildConfig.API_KEY) // API 키 추가
        }
        val placesClient = Places.createClient(this)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    TravelAssistantScreen()

                    NavManage(placesClient)
//                    TravelRecord()
//                    TravelPlanScreen(navController = )
//                    Recommendation(20, listOf("액티비티", "관광"), 0)
//                    MapScreen(placesClient)
//                    TravelAssistantScreen()
                }
            }
        }
    }
}