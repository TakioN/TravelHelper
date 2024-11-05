package com.example.travelassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelassistant.ui.theme.TravelassistantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelassistantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TravelAssistantScreen()
                }
            }
        }
    }
}

@Composable
fun TravelAssistantScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Texts
        Text(text = "Hi, 육세걸 👋", fontSize = 24.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Explore the world", fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        BasicTextField(
            value = remember { mutableStateOf(TextFieldValue("Where to go?")) }.value,
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // New Icon Buttons Row (Flights, Travel Plans, Travel Logs)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val buttonLabels = listOf("✈️ 항공", "📅 계획", "📖 기록")
            buttonLabels.forEach { label ->
                Button(
                    onClick = { /* Handle button click */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .padding(4.dp)
                ) {
                    Text(text = label)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // From and To Fields
        val fromText = remember { mutableStateOf(TextFieldValue("From")) }
        val toText = remember { mutableStateOf(TextFieldValue("To")) }

        BasicTextField(
            value = fromText.value,
            onValueChange = { fromText.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = toText.value,
            onValueChange = { toText.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Departure and Return Date Fields
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val departureText = remember { mutableStateOf(TextFieldValue("Departure")) }
            val returnText = remember { mutableStateOf(TextFieldValue("Return")) }

            BasicTextField(
                value = departureText.value,
                onValueChange = { departureText.value = it },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                value = returnText.value,
                onValueChange = { returnText.value = it },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Button
        Button(
            onClick = { /* Handle search action */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color(0xFFFFC107))
        ) {
            Text(text = "SEARCH", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Button at the Bottom
        Spacer(modifier = Modifier.weight(1f)) // Push the Profile button to the bottom
        Button(
            onClick = { /* Handle profile navigation */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "메인", fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TravelAssistantPreview() {
    TravelassistantTheme {
        TravelAssistantScreen()
    }
}
