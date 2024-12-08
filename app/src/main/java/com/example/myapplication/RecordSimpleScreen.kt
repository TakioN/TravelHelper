package com.example.myapplication

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RecordSimpleScreen(viewModel: MyViewModel, navController: NavController, btn:Int) {
    Column(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth()
            .fillMaxHeight(.6f)
    ) {


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