package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travelassistant.TravelPlanScreen

@Composable
fun NavManage() {
    val navController = rememberNavController()
    val myView: MyViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { TravelAssistantScreen(myView, navController) }
        composable("plan") { MakePlan(myView, navController) }
        composable("recommend") { RecommendPage(myView, navController) }
        composable("survey") { TravelPlanScreen(myView, navController) }
//        composable("map")
    }
}