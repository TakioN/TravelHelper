package com.example.travelassistant

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.MyViewModel
import com.example.myapplication.Recommendation
//import com.example.travelassistant.ui.theme.TravelassistantTheme
import java.util.*

@Composable
fun TravelPlanScreen(viewModel: MyViewModel = viewModel(), navController: NavHostController, ) {
    val context = LocalContext.current
    var styleList = remember { mutableStateListOf<String>() }
    var styleListNum = remember { mutableStateListOf<Int>() }
    val styleColors = listOf(
        Color(223,190,12),
        Color(133,132,127),
        Color(205, 127, 50)
    )
    val stylePlace = listOf("1st", "2nd", "3rd")
    var with by remember { mutableStateOf("") }
    var ageRange by remember { mutableStateOf(20f) }

    fun setStyleList(style:String, num: Int) {
        if(styleList.contains(style)) {
            styleList.remove(style)
            styleListNum.remove(num)
        }
        else if(styleList.size == 3) {
            styleList.removeAt(0)
            styleList.add(style)
            styleListNum.removeAt(0)
            styleListNum.add(num)
        }
        else {
            styleList.add(style)
            styleListNum.add(num)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0.1f to Color(114, 189, 235),
                    0.3f to Color.White,
                )
            )
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Travel Destination Recommendations",
            fontSize = 30.sp,
            color = Color.Black,
            lineHeight = 44.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(40.dp))

        Column(
//            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight()

        ) {


            // Age slider
            Text(text = "Age:", color = Color.Black)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Slider(
                    value = ageRange,
                    onValueChange = { ageRange = it },
                    valueRange = 20f..50f,
                    steps = 2
                )
            }
            Text(text = "Selected Age: ${ageRange.toInt()}s", color = Color.Black)

            Spacer(modifier = Modifier.height(32.dp))

            // Date picker
//            Text(text = "Date:", color = Color.Black)
//            val context = LocalContext.current
//            val calendar = Calendar.getInstance()
//            val year = calendar.get(Calendar.YEAR)
//            val month = calendar.get(Calendar.MONTH)
//            val day = calendar.get(Calendar.DAY_OF_MONTH)
//            var dateText by remember { mutableStateOf("$day / ${month + 1} / $year") }
//
//            Button(onClick = {
//                DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
//                    dateText = "$selectedDay / ${selectedMonth + 1} / $selectedYear"
//                }, year, month, day).show()
//            }) {
//                Text(text = dateText)
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))

            // Travel Style and With Whom options
            Text(text = "Travel Style:", color = Color.Black)
            Row() {
                listOf("FOOD", "SHOPPING", "HISTORY").forEachIndexed { idx, style ->
                    Button(
                        onClick = { setStyleList(style, idx) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if(styleList.contains(style)) Color.Yellow else Color.Gray
                        )
                    ) {
                        Text(text = style)
                    }
                }
            }
            Row() {
                listOf("NATURE", "CULTURE", "LANDMARK").forEachIndexed { idx, style ->
                    Button(
                        onClick = { setStyleList(style, idx + 3) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if(styleList.contains(style)) Color.Yellow else Color.Gray
                        )
                    ) {
                        Text(text = style, fontSize = 13.sp)
                    }
                }
            }
            Row() {
                listOf("ACTIVITY", "PHOTOES", "ART").forEachIndexed { idx, style ->
                    Button(
                        onClick = { setStyleList(style, idx + 6) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if(styleList.contains(style)) Color.Yellow else Color.Gray
                        )
                    ) {
                        Text(text = style)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(){
                styleList.forEachIndexed { idx, sty ->
                    Text("${stylePlace[idx]}. ${sty}", color = styleColors[idx])
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "With Whom?", color = Color.Black)
            Row() {
                listOf("FRIENDS", "FAMILY").forEach { companion ->
                    Button(
                        onClick = { with = companion },
                        modifier = Modifier.padding(end = 40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if(with == companion) Color.Yellow else Color.Gray
                        )
                    ) {
                        Text(text = companion)
                    }
                }
            }
            Row(
                modifier = Modifier.padding(start = 60.dp)
            ) {
                listOf("LOVER", "ALONE").forEach { companion ->
                    Button(
                        onClick = { with = companion },
                        modifier = Modifier.padding(end = 40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if(with == companion) Color.Yellow else Color.Gray
                        )
                    ) {
                        Text(text = companion)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Recommend and Cancel buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = {navController.popBackStack()},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Text(text = "Cancel")
                }
                Button(
                    onClick = {
                        if(with.isNotEmpty() && styleList.isNotEmpty()) {
//                            Recommendation(context, ageRange.toInt(), styleListNum, with)
                            viewModel.age = ageRange.toInt()
                            viewModel.category = styleListNum
                            viewModel.with = with
                            navController.navigate("recommend")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFC107),
//                        contentColor = Color.Black
                    )
                ) {
                    Text(text = "Recommend", color = Color.Black)
                }
            }
        }
    }
}


