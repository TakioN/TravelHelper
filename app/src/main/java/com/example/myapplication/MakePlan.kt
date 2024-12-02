package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakePlan(viewModel: MyViewModel, navController: NavController) {

    var isVisible by remember { mutableStateOf(false) }
    var destination by remember { mutableStateOf("") }
    var planList = remember { mutableStateListOf<String>("osaka") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0.1f to Color(114, 189, 235),
                    0.3f to Color.White,
                )
            )
            .padding(16.dp)
            .padding(top = 100.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(planList){item ->
                Box(
                    modifier = Modifier
                        .border(
                            width = 5.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "${viewModel.userName}의 ${item}여행",
                        fontSize = 23.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
//                        textAlign = TextAlign.Center,

                    )
                }
                Spacer(modifier = Modifier.height(16.dp))


            }
        }
        FloatingAddButton(onClick = { isVisible = true })
        if(isVisible) {
            AlertDialog(
                onDismissRequest = {isVisible = false},
                dismissButton = {
                    TextButton(onClick = {isVisible = false}) {
                        Text("Cancel")
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            planList.add(destination)
                            isVisible = false
                            destination = ""
                        }
                    ) {
                        Text("OK")
                    }
                },
                title = { Text("Where to?") },
                text = {
                    Column() {
                        OutlinedTextField(
                            value = destination,
                            onValueChange = {destination = it},
                            label = { Text("Destination") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        if(destination.isEmpty()) {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    isVisible = false
                                    navController.navigate("survey")
                                }
                            ) {
                                Text("HELP ME CHOOSE")
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun FloatingAddButton(onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.primary, // 버튼 색상
            contentColor = MaterialTheme.colorScheme.onPrimary // 아이콘 색상
        ) {
            Icon(
                imageVector = Icons.Default.Add, // 플러스 아이콘
                contentDescription = "Add"
            )
        }
    }
}