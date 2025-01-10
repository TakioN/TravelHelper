package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travelassistant.TravelPlanScreen
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

@Composable
fun NavManage(placesClient: PlacesClient) {
    val navController = rememberNavController()
    val myView: MyViewModel = viewModel()


    NavHost(navController = navController, startDestination = "home") {
        composable("home") { TravelAssistantScreen(myView, navController) }
        composable("plan") { MakePlan(myView, navController) }
        composable("recommend") { RecommendPage(myView, navController) }
        composable("survey") { TravelPlanScreen(myView, navController) }
        composable("map") { MapScreen(myView, placesClient = placesClient)}
        composable("record") { MakeRecord(myView, navController)}
        composable("read_record") { ReadRecord(myView, navController) }
        composable("write_record") { TravelRecord(myView, navController) }
    }
}