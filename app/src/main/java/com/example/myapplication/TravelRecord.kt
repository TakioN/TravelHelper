package com.example.myapplication

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

@Composable
fun TravelRecord(
//    onSave: (String, String, Uri?) -> Unit // 제목, 본문, 이미지 URI를 저장하는 콜백
    viewModel:MyViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf(viewModel.memoryTitle) }
    var content by remember { mutableStateOf(viewModel.memoryContent) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageUrl by remember { mutableStateOf<String?>(viewModel.memoryImageUrl) }


    val scrollState = rememberScrollState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
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
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("제목") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        imageUri?.let { uri ->
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = "선택한 사진",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        if(imageUri == null && imageUrl != null) {
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = "선택한 사진",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 본문 입력
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("내용") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            maxLines = 10,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 사진 추가 버튼 및 미리보기
        Button(
            onClick = { imagePickerLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("사진 추가")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 저장 버튼
        Button(
            onClick = {
                if(title.isNotEmpty() && content.isNotEmpty()){
                    saveToDb(title, content)
                    if(imageUri != null) {
                        imageUri?.let{
                            uploadImageToFirebaseStorage(it){
                                it?.let{saveImageUrlToDatabase(it, title)}
                            }
                        }
                    }
                    try {
                        // "record"가 백스택에 있는지 확인
                        navController.getBackStackEntry("record")
                        // 백스택에 "record"가 있다면 popBackStack 호출
                        navController.popBackStack("record", inclusive = false)
                    } catch (e: IllegalArgumentException) {
                        // "record"가 백스택에 없으면 navigate 호출
                        navController.navigate("record")
                    }
                }
                else {
                    Toast.makeText(context, "Please Fill Your Memory", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("저장")
        }
    }
}

fun saveToDb(title: String, content: String) {
    val database = FirebaseDatabase.getInstance()
    val databaseRef = database.reference.child("memory").child(title).child("content")

    databaseRef.setValue(content)
}
fun uploadImageToFirebaseStorage(imageUri: Uri, onComplete: (String?) -> Unit) {
    val storageRef = FirebaseStorage.getInstance().reference
    val fileRef = storageRef.child("images/${UUID.randomUUID()}")

    fileRef.putFile(imageUri)
        .addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                onComplete(uri.toString())
            }
        }
        .addOnFailureListener { exception ->
            Log.e("FirebaseStorage", "Image upload failed", exception)
            onComplete(null)
        }
}

fun saveImageUrlToDatabase(imageUrl: String, title:String) {
    val databaseRef = FirebaseDatabase.getInstance().reference.child("memory").child(title)
//    val key = databaseRef.push().key ?: return
//    databaseRef.child(key).setValue(imageUrl)
    databaseRef.child("imageUrl").setValue(imageUrl)
}


