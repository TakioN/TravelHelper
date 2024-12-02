package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.data.City
import com.example.myapplication.data.Location
import com.example.myapplication.data.airportCode

@Composable
fun RecommendPage(viewModel: MyViewModel = viewModel(), navController: NavController) {
    val context = LocalContext.current
    val age = viewModel.age
    val category = viewModel.category
    val with = viewModel.with
    val city = Recommendation(context, age, category, with)
//    Log.d("mymymy", "$age")
    val pagerState = rememberPagerState(pageCount = { 5 })
    val sortedLocations = city.locations.sortedByDescending { it.score }

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
    ) {
        Text(
            text = "${city.city}, 어떠세요?",
            fontSize = 28.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 30.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if(sortedLocations.size > 3) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ){
                HorizontalPager(
                    state = pagerState,

                ) {page ->
                    GridContent(sortedLocations.slice(0..3))
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(){
                    Button(
                        onClick = {navController.popBackStack()},
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(255, 221, 103),
                            disabledContainerColor = Color.LightGray,
                        )
                    ) {
                        Text(text = "Cancel")
                    }
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(255, 221, 103),
                            disabledContainerColor = Color.LightGray,
                        )
                    ) {
                        Text(text = "OK")
                    }
                }

            }
        }
    }
}

@Composable
fun GridContent(gridItems: List<Location>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        items(gridItems) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        BorderStroke(2.dp, Color.Gray), // 테두리 두께와 색상
                        shape = RoundedCornerShape(8.dp) // 테두리 모양
                    )
                    .aspectRatio(1.0f)
            ){
                Text(
                    text = "${it.name}",
                    fontSize = 25.sp,


                )
            }

        }
    }
}