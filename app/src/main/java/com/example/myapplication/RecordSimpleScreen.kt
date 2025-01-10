package com.example.myapplication

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun RecordSimpleScreen(viewModel: MyViewModel, navController: NavController, btn:Int) {

    var isLoading by remember { mutableStateOf(true) }
    var memoryList = remember{ mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        getTitles{titles->
            memoryList.clear()
            memoryList.addAll(titles)
            isLoading = false
        }
    }

    if (isLoading) {
        // 로딩 화면 표시
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.6f),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    else {
        LazyColumn(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxWidth()
                .fillMaxHeight(.6f)
                .padding(3.dp)
        ) {
            items(memoryList){item->
                Box(
                    modifier = Modifier
                        .border(
                            width = 5.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(5.dp)
                        .clickable {
                            viewModel.memoryTitle = item
                            navController.navigate("read_record")
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = item,
                        fontSize = 23.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
//                        textAlign = TextAlign.Center,

                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {
            navController.navigate("record")
            viewModel.selectedBtn = btn
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(255, 221, 103),
            disabledContainerColor = Color.LightGray,
        )
    ) {
        Text(text = "GO TO MEMORY")
    }
}