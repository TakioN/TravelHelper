package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(placesClient: PlacesClient) {
    val singapore = LatLng(37.7749, -122.4194)
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(singapore, 13f)
    }
    val markerState = remember{MarkerState(position = LatLng(37.7749, -122.4194))}
    var selectedText by remember { mutableStateOf("") }
    var searchQuery by remember {
        mutableStateOf(TextFieldValue())
    }
    var isVisible by remember { mutableStateOf(true) }
//    var markerPositions by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    var markerPositions = remember { mutableStateListOf<LatLng>() }
    val markerPosition = remember { mutableStateOf<LatLng?>(null) }
    var searchResults by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    var isBtnActive by remember { mutableStateOf(false) }
//    var selectedPlace by remember { mutableStateOf<AutocompletePrediction?>(null) }
    var selectedPlaceId by remember { mutableStateOf("") }
    var selectedPlaces = remember { mutableStateListOf<String>() }

//    LaunchedEffect(searchQuery) {
//        if (searchQuery.isNotEmpty()) {
//            searchPlace(searchQuery, placesClient) { resultLatLng, predictions ->
//                searchResults = predictions
//                if (resultLatLng != null) {
//                    markerPositions = markerPositions + resultLatLng
//                }
////                resultLatLng?.let {
////                    cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
////                }
//            }
//        } else {
//            searchResults = emptyList() // 검색어가 비어있으면 결과 초기화
//        }
//    }

    Column{
        Box{
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
//                .height(Dp(300f)),
                cameraPositionState = cameraPositionState
            ) {
                markerPositions.forEachIndexed{ idx,latlng ->
                    Marker(
                        state = MarkerState(latlng),
                        icon = createCircularNumberedMarkerIcon((idx + 1).toString())
                    )

                }
//                Marker(
//                    state = markerState,
//                    icon = createCircularNumberedMarkerIcon("1"),
//                    title = "temp",
//                    snippet = "te"
//                )
            }
            Column{
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            searchPlace(it.text, placesClient) { latLng, predictionList ->
                                searchResults = predictionList // 자동 완성 결과 업데이트
                                if (latLng != null) {
                                    markerPosition.value = latLng
                                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
                                }
                            }
                            isVisible = true
                            isBtnActive = false
                        },
                        modifier = Modifier
//                    .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp),
                        label = { Text("Search a place") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )
                    Button(
                        onClick = {
                            if(selectedPlaceId.isNotEmpty()){
                                placesClient.fetchPlace(
                                    FetchPlaceRequest.builder(
                                        selectedPlaceId,
                                        listOf(Place.Field.LOCATION)
                                    ).build()
                                ).addOnSuccessListener { fetchResponse ->
                                    val latLng = fetchResponse.place.location
                                    if (latLng != null) {
//                                        markerPosition.value = latLng
                                        markerPositions.add(latLng)
                                        cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
                                    }
                                }
                                selectedPlaces.add(searchQuery.text)
                            }
                            isBtnActive = false
                            searchQuery = TextFieldValue()
                        },
                        contentPadding = PaddingValues(0.dp),
                        enabled = isBtnActive

//                    .v
                    ){
                        Text(
                            text = "ADD",
                            fontSize = 12.sp
                        )
                    }
                }


                if (searchQuery.text.isNotEmpty() && isVisible) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
//                items(searchResults) { prediction ->
//                    Text(
//                        text = prediction.getPrimaryText(null).toString(),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(4.dp)
//                            .clickable {
//                                // 클릭 시 해당 장소를 마커 리스트에 추가
//                                val placeLatLng = prediction.latLng
//                                if (placeLatLng != null) {
//                                    markerPositions = markerPositions + placeLatLng
//                                    cameraPositionState.position = CameraPosition.fromLatLngZoom(placeLatLng, 15f)
//                                    searchQuery = "" // 검색창 초기화
//                                    searchResults = emptyList() // 결과 리스트 닫기
//                                }
//                            }
//                    )
//                }
                        items(searchResults){prediction ->
                            ListItem(
                                modifier = Modifier.clickable {
                                    // 선택된 장소의 좌표를 가져와 마커를 표시
//                                    val placeId = prediction.placeId
                                    selectedText = prediction.getPrimaryText(null).toString()
                                    selectedPlaceId = prediction.placeId

                                    searchQuery = TextFieldValue(
                                        text = selectedText,
                                        selection = TextRange(selectedText.length)
                                    )
                                    isVisible = false
                                    isBtnActive = true
                                },
                                headlineContent = { Text(prediction.getPrimaryText(null).toString()) },
                                supportingContent = {Text(text = prediction.getSecondaryText(null).toString())}
                            )
                        }
                    }
                }
            }


//        Button(
//            onClick = {
//                searchPlace(searchQuery, placesClient) { resultLatLng ->
//                    markerPosition.value = resultLatLng
//                    resultLatLng?.let {
//                        cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
//                    }
//                }
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp)
//        ) {
//            Text("Search")
//        }

        }
        LazyColumn(
            modifier = Modifier.padding(8.dp)
        ) {
            itemsIndexed(selectedPlaces) {idx, item ->
                ListItem(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp)),
                    headlineContent = {Text(item)},
                    leadingContent = {
                        Text(
                            text = (idx + 1).toString(), // 순서 번호
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (idx % 2 == 0) Color.White else Color.LightGray,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = if (idx % 2 == 0) Color.LightGray else Color.White,
                        headlineColor = Color.Black
                    )

                )
                if (idx < selectedPlaces.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }


}

fun createCircularNumberedMarkerIcon(number: String): BitmapDescriptor {
    val ts = 48f // 텍스트 크기
    val circleRadius = 35f // 원형의 반지름
    val paint = Paint().apply {
        isAntiAlias = true
        textSize = ts
        textAlign = Paint.Align.CENTER
    }

    // 텍스트와 원 배경 색상
    val circlePaint = Paint().apply {
        color = Color.Blue.toArgb() // 원의 배경색
        isAntiAlias = true
    }
    val textPaint = Paint(paint).apply {
        color = Color.White.toArgb() // 텍스트 색상
    }

    // 비트맵 생성
    val bitmapDiameter = (circleRadius * 2).toInt()
    val bitmap = Bitmap.createBitmap(bitmapDiameter, bitmapDiameter, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // 원형 배경 그리기
    canvas.drawCircle(
        circleRadius,
        circleRadius,
        circleRadius,
        circlePaint
    )

    // 텍스트 위치 계산 및 그리기
    val textBounds = Rect()
    textPaint.getTextBounds(number, 0, number.length, textBounds)
    val textHeight = textBounds.height()
    canvas.drawText(
        number,
        circleRadius, // 가로 중심
        circleRadius + (textHeight / 2f), // 세로 중심
        textPaint
    )

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun searchPlace(query: String, placesClient: PlacesClient, onResult: (LatLng?, List<AutocompletePrediction>) -> Unit) {
    val token = AutocompleteSessionToken.newInstance()
    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .setSessionToken(token)
        .build()

    placesClient.findAutocompletePredictions(request)
        .addOnSuccessListener { response ->
            val predictions = response.autocompletePredictions
            if (predictions.isNotEmpty()) {
                val firstPrediction = predictions.first()
                val placeId = firstPrediction.placeId
                placesClient.fetchPlace(
                    com.google.android.libraries.places.api.net.FetchPlaceRequest.builder(
                        placeId,
                        listOf(com.google.android.libraries.places.api.model.Place.Field.LAT_LNG)
                    ).build()
                ).addOnSuccessListener { fetchResponse ->
                    onResult(fetchResponse.place.location, predictions)
                }.addOnFailureListener {
                    onResult(null, predictions)
                }
            } else {
                onResult(null, predictions)
            }
        }
        .addOnFailureListener {
            onResult(null, emptyList())
        }
}
