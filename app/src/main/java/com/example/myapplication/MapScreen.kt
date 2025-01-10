package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MapScreen(viewModel:MyViewModel = viewModel(), placesClient: PlacesClient) {
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
    var markerPositions = remember { mutableStateListOf<LatLng>()}
//        LatLng(34.6872571, 135.5258546),
//                        LatLng(34.7024854, 135.4959506)) }
//    var markerPositions = remember{ mutableStateListOf<MutableState<MutableList<LatLng>>>() }
    val markerPosition = remember { mutableStateOf<LatLng?>(null) }
    var searchResults by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    var isBtnActive by remember { mutableStateOf(false) }
//    var selectedPlace by remember { mutableStateOf<AutocompletePrediction?>(null) }
    var selectedPlaceId by remember { mutableStateOf("") }
    var selectedPlaces = remember { mutableStateListOf<String>() }

    //page state
    var planLists = remember{ mutableStateListOf<MutableList<String>>() }
    var planList = remember{ mutableStateListOf<String>() }
    var totalPage by remember{ mutableStateOf(1) }
    val pagerState = rememberPagerState{totalPage}
//    val pagerState = rememberPagerState(pageCount = {2})
    var currentPage by remember{ mutableStateOf(0) }

//    firebase database
    val database = FirebaseDatabase.getInstance()
    val city = viewModel.cityName
    val tripRef = database.reference.child("trips").child(city)

    if(planLists.isEmpty()) {
        planLists.add(mutableListOf())
    }

    LaunchedEffect(Unit) {
        getLatLngListForPage(currentPage, city) { latlngItem, placeItem ->
            markerPositions.clear()
            selectedPlaces.clear()
            markerPositions.addAll(latlngItem)
            selectedPlaces.addAll(placeItem)
            if(markerPositions.isNotEmpty()) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(markerPositions.first(), 15f)
                totalPage++
            }

        }
    }
    
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collectLatest { pageIndex ->
            currentPage = pageIndex
            getLatLngListForPage(pageIndex, city) { latlngItem, placeItem ->
                markerPositions.clear()
                selectedPlaces.clear()
                markerPositions.addAll(latlngItem)
                selectedPlaces.addAll(placeItem)
                if(pageIndex + 1 == totalPage && markerPositions.isNotEmpty()){
                    totalPage++
                }
            }
        }
    }

    Column{
        Box{
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
//                .height(Dp(300f)),
                cameraPositionState = cameraPositionState
            ) {
//                markerPositions.forEachIndexed{ idx,latlng ->
                markerPositions.forEachIndexed{ idx,latlng ->
                    Marker(
                        state = MarkerState(latlng),
                        icon = createCircularNumberedMarkerIcon((idx + 1).toString())
                    )

                }
                Polyline(
                    points = markerPositions.toList(),
                    color = Color.Blue,
                    width = 15f,
                    jointType = com.google.android.gms.maps.model.JointType.ROUND,
                    pattern = listOf(
                        com.google.android.gms.maps.model.Dot(), // 점선의 점을 만듦
                        com.google.android.gms.maps.model.Gap(10f) // 점선 간격을 설정
                    )
                )

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
                                        val latLngMap = mapOf(
                                            "latitude" to latLng.latitude,
                                            "longitude" to latLng.longitude
                                        )
                                        tripRef.child("day${currentPage + 1}").child("latLngs")
                                            .push().setValue(latLngMap)
                                        cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
                                    }
                                }
                                tripRef.child("day${currentPage + 1}").child("name").push().setValue(searchQuery.text)

                            }
                            isBtnActive = false
                            searchQuery = TextFieldValue()
                        },
                        contentPadding = PaddingValues(0.dp),
                        enabled = isBtnActive
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

        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 8.dp)
        ){
            Text(
                text = "Day ${currentPage + 1}",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp
            )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
            ) {pageIdx ->
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
                                    text = (idx + 1).toString(),
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

fun getLatLngListForPage(page: Int, city:String, callback: (List<LatLng>, List<String>) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val pagePath = "trips/${city}/day${page + 1}"
    val latLngsRef = database.getReference(pagePath)
    val latLngList = mutableListOf<LatLng>()
    val placeList = mutableListOf<String>()

    latLngsRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            latLngList.clear()
            placeList.clear()
            for (childSnapshot in snapshot.child("latLngs").children) {
                val longitude = childSnapshot.child("longitude").getValue(Double::class.java)
                val latitude = childSnapshot.child("latitude").getValue(Double::class.java)

                if (longitude != null && latitude != null) {
                    latLngList.add(LatLng(latitude, longitude))
                }
            }
            for(childSnapshot in snapshot.child("name").children) {
                val place = childSnapshot.getValue(String::class.java)
                if(place != null) {
                    placeList.add(place)
                }
            }
            callback(latLngList, placeList)
        }

        override fun onCancelled(error: DatabaseError) {
            println("Error: ${error.message}")
            callback(emptyList(), emptyList())
        }
    })
}
