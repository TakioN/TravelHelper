package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.data.airportCode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    return formatter.format(Date(millis))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelAssistantScreen(viewModel: MyViewModel = viewModel(), navController: NavController) {
    val context = LocalContext.current
    var selectedBtn by remember { mutableStateOf(viewModel.selectedBtn) }
    val focusManager = LocalFocusManager.current

    var isFocused by remember { mutableStateOf(false) }
    var fromText by remember { mutableStateOf("") }
    var toText by remember { mutableStateOf("") }
    var departureText by remember { mutableStateOf("") }
    var returnText by remember { mutableStateOf("") }
    var isOneway by remember { mutableStateOf(true) }


    var showDatePickerOne by remember { mutableStateOf(false) }
    var datePickerStateOne by remember { mutableStateOf<Long?>(null) }
    val selectedDateOne =
        datePickerStateOne?.let { convertMillisToDate(datePickerStateOne!!) } ?: ""

    var showDatePickerRound by remember { mutableStateOf(false) }
    var datePickerStateRoundFrom by remember { mutableStateOf<Long?>(null) }
    val selectedDateRoundFrom =
        datePickerStateRoundFrom?.let { convertMillisToDate(datePickerStateRoundFrom!!) } ?: ""
    var datePickerStateRoundTo by remember { mutableStateOf<Long?>(null) }
    val selectedDateRoundTo =
        datePickerStateRoundTo?.let { convertMillisToDate(datePickerStateRoundTo!!) } ?: ""

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
        // Header Texts
        Text(
            text = "Hi, ${viewModel.userName} 👋",
            fontSize = 28.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 30.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Explore the world",
            fontSize = 18.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 15.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
//        OutlinedTextField(
//            value = destination,
//            onValueChange = { destination = it },
//            label = { if(!isFocused && destination.isEmpty()) Text("Where to go?") },
//            singleLine = true,
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Search,
//                    contentDescription = "Search Icon"
//                )
//            },
//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                focusedTextColor = Color.Black,
//                unfocusedTextColor = Color.Black,
//                containerColor = Color.White,
//                focusedLabelColor = Color.Black
//            ),
//            shape = RoundedCornerShape(25.dp),
//            modifier = Modifier
//                .onFocusChanged { focusState -> isFocused = focusState.isFocused }
//                .fillMaxWidth()
//        )


        Spacer(modifier = Modifier.height(16.dp))

        // New Icon Buttons Row (Flights, Travel Plans, Travel Logs)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val buttonLabels = listOf("Flights", "Plan", "Record")
            val icons = listOf("flight", "calender", "book")
            buttonLabels.forEachIndexed { idx, label ->
                val vectorId = context.resources.getIdentifier(
                    icons[idx], "drawable", context.packageName
                )
                Button(
                    onClick = {selectedBtn = idx},
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(4.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(idx == selectedBtn)Color(42, 123, 175) else Color.Gray,
                        disabledContainerColor = Color.LightGray,
                        contentColor = Color.White
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            painter = painterResource(id = vectorId),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                        )
                        Text(
                            text = label,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.End
//        ) {
//            Text(
//                text = if (isOneway) "One-Way" else "Round-Trip",
//                color = Color.Black
//            )
//            Switch(
//                checked = isOneway,
//                onCheckedChange = { isOneway = it },
//                modifier = Modifier.padding(start = 8.dp),
//                colors = SwitchDefaults.colors(
//                    uncheckedTrackColor = Color(42, 123, 175)
//                )
//            )
//        }
//
//        // From and To Fields
//        OutlinedTextField(
//            value = fromText,
//            onValueChange = { fromText = it },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 10.dp),
//            placeholder = { Text(text = "FROM") },
//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                focusedTextColor = Color.Black,
//                unfocusedTextColor = Color.Black,
//                containerColor = Color(244, 235, 235),
//                focusedBorderColor = Color.Transparent,
//                unfocusedBorderColor = Color.Transparent
//            )
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        OutlinedTextField(
//            value = toText,
//            onValueChange = { toText = it },
//            modifier = Modifier
//                .fillMaxWidth(),
//            placeholder = { Text(text = "TO") },
//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                focusedTextColor = Color.Black,
//                unfocusedTextColor = Color.Black,
//                containerColor = Color(244, 235, 235),
//                focusedBorderColor = Color.Transparent,
//                unfocusedBorderColor = Color.Transparent
//            )
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Departure and Return Date Fields
//        if (isOneway) {
//            OutlinedTextField(
//                value = selectedDateOne,
//                readOnly = true,
//                onValueChange = { departureText = it },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable {
//                        focusManager.clearFocus()
//
//                    },
//                placeholder = { Text(text = "DEPARTURE") },
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    focusedTextColor = Color.Black,
//                    unfocusedTextColor = Color.Black,
//                    containerColor = Color(244, 235, 235),
//                    focusedBorderColor = Color.Transparent,
//                    unfocusedBorderColor = Color.Transparent
//                ),
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.DateRange,
//                        contentDescription = null,
//                        tint = Color.Black,
//                        modifier = Modifier.clickable {
//                            showDatePickerOne = !showDatePickerOne
//                        }
//                    )
//                }
//            )
//            if (showDatePickerOne) {
//                DatePickerModal(
//                    onDateSelected = {
//                        datePickerStateOne = it
//                    },
//                    onDismiss = { showDatePickerOne = false }
//                )
//            }
//        } else {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//
//                OutlinedTextField(
//                    value = selectedDateRoundFrom,
//                    readOnly = true,
//                    onValueChange = { departureText = it },
//                    modifier = Modifier
//                        .weight(1f)
//                        .clickable { focusManager.clearFocus() },
//                    placeholder = { Text(text = "DEPARTURE") },
//                    colors = TextFieldDefaults.outlinedTextFieldColors(
//                        focusedTextColor = Color.Black,
//                        unfocusedTextColor = Color.Black,
//                        containerColor = Color(244, 235, 235),
//                        focusedBorderColor = Color.Transparent,
//                        unfocusedBorderColor = Color.Transparent
//                    ),
//                    leadingIcon = {
//                        Icon(
//                            imageVector = Icons.Default.DateRange,
//                            contentDescription = null,
//                            tint = Color.Black,
//                            modifier = Modifier.clickable {
//                                showDatePickerRound = !showDatePickerRound
//                            }
//                        )
//                    }
//                )
//
//                Spacer(modifier = Modifier.width(8.dp))
//
//                OutlinedTextField(
//                    value = selectedDateRoundTo,
//                    readOnly = true,
//                    onValueChange = { returnText = it },
//                    modifier = Modifier
//                        .weight(1f),
//                    placeholder = { Text(text = "RETURN") },
//                    colors = TextFieldDefaults.outlinedTextFieldColors(
//                        focusedTextColor = Color.Black,
//                        unfocusedTextColor = Color.Black,
//                        containerColor = Color(244, 235, 235),
//                        focusedBorderColor = Color.Transparent,
//                        unfocusedBorderColor = Color.Transparent
//                    ),
//                    leadingIcon = {
//                        Icon(
//                            imageVector = Icons.Default.DateRange,
//                            contentDescription = null,
//                            tint = Color.Black,
//                            modifier = Modifier.clickable {
//                                showDatePickerRound = !showDatePickerRound
//                            }
//                        )
//                    }
//                )
//                if (showDatePickerRound) {
//                    DateRangePickerModal(
//                        onDateRangeSelected = {
//                            datePickerStateRoundFrom = it.first
//                            datePickerStateRoundTo = it.second
//                        },
//                        onDismiss = { showDatePickerRound = false }
//                    )
//                }
//            }
//
//
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Search Button
//        Button(
//            onClick = {
//                val depCode = airportCode[fromText]
//                val arrCode = airportCode[toText]
//                var webPage:Uri? = null
//                if (isOneway) {
//                    webPage =
//                        Uri.parse("https://flight.naver.com/flights/international/${depCode}-${arrCode}-${selectedDateOne}?adult=1&fareType=Y")
//                } else {
//                    webPage =
//                        Uri.parse("https://flight.naver.com/flights/international/${depCode}-${arrCode}-${selectedDateRoundFrom}/${arrCode}-${depCode}-${selectedDateRoundTo}?adult=1&fareType=Y")
//                }
//                val intent = Intent(Intent.ACTION_VIEW, webPage)
//                if (fromText.isNotEmpty()
//                    && toText.isNotEmpty()
//                    && (selectedDateOne.isNotEmpty() || (selectedDateRoundFrom.isNotEmpty() && selectedDateRoundTo.isNotEmpty())))
//                    context.startActivity(intent)
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(48.dp),
//            shape = RoundedCornerShape(20.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(255, 221, 103),
//                disabledContainerColor = Color.LightGray,
//            )
//        ) {
//            Text(text = "SEARCH")
//        }
        if(selectedBtn == 0)
            FlightSimpleScreen()
        else if(selectedBtn == 1)
            PlanSimpleScreen(viewModel, navController, selectedBtn)

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Button at the Bottom
        Spacer(modifier = Modifier.weight(1f)) // Push the Profile button to the bottom
//        Button(
//            onClick = { /* Handle profile navigation */ },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(48.dp)
//        ) {
//            Text(text = "메인", fontSize = 16.sp)
//        }
    }
}