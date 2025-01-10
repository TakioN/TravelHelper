package com.example.myapplication

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun ReadRecord(viewModel: MyViewModel = viewModel(), navController: NavController) {
    var title by remember { mutableStateOf(viewModel.memoryTitle) }
    var content by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

//    val imagePickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        imageUri = uri
//    }

    LaunchedEffect(Unit) {
        getContent(title) {
            content = it
        }
        fetchImageUrlsFromDatabase(title){uri ->
            imageUri = uri
        }
    }

    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0.1f to Color(114, 189, 235),
                    0.3f to Color.White,
                )
            )
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "A Piece of Trip",
            fontSize = 30.sp,
            color = Color.Black,
            lineHeight = 44.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 제목 입력
//        OutlinedTextField(
//            value = title,
//            onValueChange = { title = it },
//            label = { Text("제목") },
//            modifier = Modifier.fillMaxWidth()
//        )
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontSize = 30.sp,
            fontFamily = FontFamily.SansSerif
        )

        imageUri?.let { uri ->
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = "선택한 사진",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 본문 입력
        Text(text = content,
            modifier = Modifier
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(12.dp)
                .fillMaxWidth(),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 사진 추가 버튼 및 미리보기
        Button(
            onClick = {
                viewModel.memoryContent = content
                viewModel.memoryImageUrl = imageUri
                navController.navigate("write_record")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("수정")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 저장 버튼
        Button(
            onClick = {
                try {
                    // "record"가 백스택에 있는지 확인
                    navController.getBackStackEntry("record")
                    // 백스택에 "record"가 있다면 popBackStack 호출
                    navController.popBackStack("record", inclusive = false)
                } catch (e: IllegalArgumentException) {
                    // "record"가 백스택에 없으면 navigate 호출
                    navController.navigate("record")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("확인")
        }
    }
}

fun getContent(title:String, callback: (String) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val databaseRef = database.reference.child("memory").child(title).child("content")

    databaseRef.addListenerForSingleValueEvent(object:ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val data = snapshot.getValue(String::class.java)
            if(data != null) {
                callback(data)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            callback("")
        }

    })
}

fun fetchImageUrlsFromDatabase(title:String, onComplete: (String?) -> Unit) {
    val databaseRef = FirebaseDatabase.getInstance().reference.child("memory").child(title)

    databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)
            onComplete(imageUrl)

        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("FirebaseDatabase", "Error fetching image URLs", error.toException())
        }
    })
}