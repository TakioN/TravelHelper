package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.myapplication.data.airportCode
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter.BASIC_ISO_DATE
import java.util.Date
import java.util.Locale

fun gotoBook(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(context, intent, null)
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    return formatter.format(Date(millis))
}
//fun convertMillisToDateR(dateRange: Pair<Long, Long>): Pair<String, String> {
//    val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
//    val firstDate = formatter.format(Date(dateRange.first))
//    val secondDate = formatter.format(Date(dateRange.second))
//    return Pair<firstDate, secondDate>
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    //places
    val places = listOf("인천", "오사카", "도쿄", "울란바토르", "베이징", "인상")

    var queryDep by remember { mutableStateOf("") }
    var queryArr by remember { mutableStateOf("") }
    val searchListDep = places.filter {
        it.contains(queryDep) && queryDep.isNotEmpty()
    }
    val searchListArr = places.filter {
        it.contains(queryArr) && queryArr.isNotEmpty()
    }
    var isVisibleDep by remember {
        mutableStateOf(true)
    }
    var isVisibleArr by remember {
        mutableStateOf(true)
    }
    var isOneway by remember { mutableStateOf(true) }

    var showDatePickerOne by remember { mutableStateOf(false) }
    var datePickerStateOne by remember { mutableStateOf<Long?>(null) }
    val selectedDateOne =
        datePickerStateOne?.let { convertMillisToDate(datePickerStateOne!!) } ?: ""
    var showDatePickerRound by remember { mutableStateOf(false) }
    var datePickerStateRound by remember { mutableStateOf<Pair<Long?, Long?>>(null to null) }
//    val selectedDateRound =
//        datePickerStateRound?.let { convertMillisToDateR(datePickerStateRound!!) } ?: ""
//    var datePickerStateRoundS by remember { mutableStateOf<Long?>(null) }
//    var datePickerStateRoundE by remember { mutableStateOf<Long?>(null) }
//    var selectedDateRoundS =
//        datePickerStateRoundS?.let { convertMillisToDate(datePickerStateRoundS!!) } ?: ""
//    var selectedDateRoundE =
//        datePickerStateRoundE?.let { convertMillisToDate(datePickerStateRoundE!!) } ?: ""

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    0.1f to Color(114, 189, 235),
                    0.3f to Color.White,
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row() {
            Button(
                modifier = Modifier.padding(end = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isOneway) MaterialTheme.colorScheme.primary else Color.Gray
                ),
                onClick = { isOneway = true }
            ) { Text("편도") }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isOneway) Color.Gray else MaterialTheme.colorScheme.primary
                ),
                onClick = { isOneway = false }
            ) { Text("왕복") }
        }
        Column() {
            OutlinedTextField(
                value = queryDep,
                label = { Text("From") },
                colors = TextFieldDefaults.outlinedTextFieldColors(focusedTextColor = Color.Black),
                onValueChange = {
                    queryDep = it
                    isVisibleDep = true
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            if (isVisibleDep && searchListDep.isNotEmpty()) {
                LazyColumn {
                    items(searchListDep) { place ->
                        Text(
                            text = place,
                            modifier = Modifier
                                .clickable {
                                    queryDep = place
                                    isVisibleDep = false
                                    focusManager.clearFocus()
                                }
                                .padding(5.dp),
                            color = Color.Black,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
        Column() {
            OutlinedTextField(
                value = queryArr,
                label = { Text("To") },
                colors = TextFieldDefaults.outlinedTextFieldColors(focusedTextColor = Color.Black),
                onValueChange = {
                    queryArr = it
                    isVisibleArr = true
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            if (isVisibleArr && searchListArr.isNotEmpty()) {
                LazyColumn {
                    items(searchListArr) { place ->
                        Text(
                            text = place,
                            modifier = Modifier
                                .width(200.dp)
                                .clickable {
                                    queryArr = place
                                    isVisibleArr = false
                                    focusManager.clearFocus()
                                }
                                .padding(5.dp),
                            color = Color.Black,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
        if (isOneway) {
            OutlinedTextField(
                value = selectedDateOne,
                onValueChange = { },
                label = { Text("Date") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePickerOne = !showDatePickerOne }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                },
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth(0.8f),
                colors = TextFieldDefaults.outlinedTextFieldColors(focusedTextColor = Color.Black)
            )
            if (showDatePickerOne) {
                DatePickerModal(
                    onDateSelected = {
                        datePickerStateOne = it
                    },
                    onDismiss = { showDatePickerOne = false }
                )
            }
        } else {
            Row(Modifier.fillMaxWidth(0.8f)) {
                OutlinedTextField(
                    value = selectedDateOne,
                    onValueChange = { },
                    label = { Text("Date") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePickerRound = !showDatePickerRound }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select date"
                            )
                        }
                    },
                    modifier = Modifier
                        .height(64.dp)
                        .fillMaxWidth(0.5f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(focusedTextColor = Color.Black)
                )
                OutlinedTextField(
                    value = selectedDateOne,
                    onValueChange = { },
                    label = { Text("Date") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePickerRound = !showDatePickerRound }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select date"
                            )
                        }
                    },
                    modifier = Modifier
                        .height(64.dp)
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(focusedTextColor = Color.Black)
                )
            }
            if (showDatePickerRound) {
                DateRangePickerModal(
                    onDateRangeSelected = {

                    },
                    onDismiss = {showDatePickerRound = false}
                )
            }
        }


        Button(
            modifier = Modifier
                .padding(top = 25.dp),
            onClick = {
                val depCode = airportCode[queryDep]
                val arrCode = airportCode[queryArr]
//                val webPage =
//                    Uri.parse("https://flight.naver.com/flights/international/${depCode}-${arrCode}-20241111/${arrCode}-${depCode}-20241114?adult=1&fareType=Y")
                val webPage =
                    Uri.parse("https://flight.naver.com/flights/international/${depCode}-${arrCode}-${selectedDateOne}?adult=1&fareType=Y")
                val intent = Intent(Intent.ACTION_VIEW, webPage)
                context.startActivity(intent)
            }
        ) {
            Text("항공권 예약")
        }
    }
}