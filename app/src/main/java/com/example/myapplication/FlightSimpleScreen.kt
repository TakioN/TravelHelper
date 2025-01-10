package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.airportCode




@Composable
fun FlightSimpleScreen() {

    val context = LocalContext.current
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

    var adultExpanded by remember { mutableStateOf(false) }
    var childExpanded by remember { mutableStateOf(false) }
    var infantExpanded by remember { mutableStateOf(false) }
    var aSelectedOption by remember { mutableStateOf(1) }
    var cSelectedOption by remember { mutableStateOf(0) }
    var iSelectedOption by remember { mutableStateOf(0) }
    var adultText by remember { mutableStateOf("성인") }
    var childText by remember { mutableStateOf("소인") }
    var infantText by remember { mutableStateOf("유아") }
    val options = listOf(0,1,2,3,4,5)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = if (isOneway) "One-Way" else "Round-Trip",
            color = Color.Black
        )
        Switch(
            checked = isOneway,
            onCheckedChange = { isOneway = it },
            modifier = Modifier.padding(start = 8.dp),
            colors = SwitchDefaults.colors(
                uncheckedTrackColor = Color(42, 123, 175)
            )
        )
    }

    // From and To Fields
    OutlinedTextField(
        value = fromText,
        onValueChange = { fromText = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),

        placeholder = { Text(text = "FROM") },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedContainerColor = Color(244, 235, 235),
            unfocusedContainerColor = Color(244, 235, 235),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = toText,
        onValueChange = { toText = it },
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = { Text(text = "TO") },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedContainerColor = Color(244, 235, 235),
            unfocusedContainerColor = Color(244, 235, 235),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )

    Spacer(modifier = Modifier.height(16.dp))

    Row(modifier = Modifier.fillMaxWidth()){
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = adultText,
                modifier = Modifier.clickable { adultExpanded = true },
                color = Color.Black
            )
            DropdownMenu(
                expanded = adultExpanded,
                onDismissRequest = { adultExpanded = false }
            ) {
                options.drop(1).forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            aSelectedOption = option
                            adultText = "${aSelectedOption}명"
                            adultExpanded = false
                        },
                        text = {Text(text = "${option}명")}
                    )
                }
            }
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = childText,
                modifier = Modifier.clickable { childExpanded = true },
                color = Color.Black
            )
            DropdownMenu(
                expanded = childExpanded,
                onDismissRequest = { childExpanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            cSelectedOption = option
                            childText = "${cSelectedOption}명"
                            childExpanded = false
                        },
                        text = {Text(text = "${option}명")}
                    )
                }
            }
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = infantText,
                modifier = Modifier.clickable { infantExpanded = true },
                color = Color.Black
            )
            DropdownMenu(
                expanded = infantExpanded,
                onDismissRequest = { infantExpanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            iSelectedOption = option
                            infantText = "${iSelectedOption}명"
                            infantExpanded = false
                        },
                        text = {Text(text = "${option}명")}
                    )
                }
            }
        }

    }

    Spacer(modifier = Modifier.height(16.dp))

    // Departure and Return Date Fields
    if (isOneway) {
        OutlinedTextField(
            value = selectedDateOne,
            readOnly = true,
            onValueChange = { departureText = it },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    focusManager.clearFocus()

                },
            placeholder = { Text(text = "DEPARTURE") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color(244, 235, 235),
                unfocusedContainerColor = Color(244, 235, 235),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.clickable {
                        showDatePickerOne = !showDatePickerOne
                    }
                )
            }
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            OutlinedTextField(
                value = selectedDateRoundFrom,
                readOnly = true,
                onValueChange = { departureText = it },
                modifier = Modifier
                    .weight(1f)
                    .clickable { focusManager.clearFocus() },
                placeholder = { Text(text = "DEPARTURE") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color(244, 235, 235),
                    unfocusedContainerColor = Color(244, 235, 235),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.clickable {
                            showDatePickerRound = !showDatePickerRound
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = selectedDateRoundTo,
                readOnly = true,
                onValueChange = { returnText = it },
                modifier = Modifier
                    .weight(1f),
                placeholder = { Text(text = "RETURN") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color(244, 235, 235),
                    unfocusedContainerColor = Color(244, 235, 235),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.clickable {
                            showDatePickerRound = !showDatePickerRound
                        }
                    )
                }
            )
            if (showDatePickerRound) {
                DateRangePickerModal(
                    onDateRangeSelected = {
                        datePickerStateRoundFrom = it.first
                        datePickerStateRoundTo = it.second
                    },
                    onDismiss = { showDatePickerRound = false }
                )
            }
        }


    }

    Spacer(modifier = Modifier.height(16.dp))

    // Search Button
    Button(
        onClick = {
            val depCode = airportCode[fromText]
            val arrCode = airportCode[toText]
            var webPage: Uri? = null
            val member = getMember(aSelectedOption, cSelectedOption, iSelectedOption)
            if (isOneway) {
                webPage =
                    Uri.parse("https://flight.naver.com/flights/international/${depCode}-${arrCode}-${selectedDateOne}?${member}&fareType=Y")
            } else {
                webPage =
                    Uri.parse("https://flight.naver.com/flights/international/${depCode}-${arrCode}-${selectedDateRoundFrom}/${arrCode}-${depCode}-${selectedDateRoundTo}?${member}&fareType=Y")
            }
            val intent = Intent(Intent.ACTION_VIEW, webPage)
            if (fromText.isNotEmpty()
                && toText.isNotEmpty()
                && (selectedDateOne.isNotEmpty() || (selectedDateRoundFrom.isNotEmpty() && selectedDateRoundTo.isNotEmpty())))
                context.startActivity(intent)
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
        Text(text = "SEARCH")
    }
}

fun getMember(a:Int, c:Int, i:Int):String {
    var result = "adult=${a}"
    if(c != 0) result += "&child=${c}"
    if(i != 0) result += "&infant=${i}"

    return result
}