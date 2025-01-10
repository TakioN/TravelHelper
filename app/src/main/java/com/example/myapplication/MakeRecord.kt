package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class MemoryEntry(
    val content: String =""
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeRecord(viewModel: MyViewModel, navController: NavController) {

    var isVisible by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var memoryList = remember{ mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        getTitles{titles->
            memoryList.clear()
            memoryList.addAll(titles)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0.1f to Color(114, 189, 235),
                    0.3f to Color.White,
                )
            )
            .padding(16.dp)
            .padding(top = 100.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(memoryList){item ->
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
        FloatingAddButton(onClick = { isVisible = true })

        if(isVisible) {
            AlertDialog(
                onDismissRequest = {isVisible = false},
                dismissButton = {
                    TextButton(onClick = {isVisible = false}) {
                        Text("Cancel")
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.memoryTitle = title
                            viewModel.memoryContent = ""
                            viewModel.memoryImageUrl = null
//                            saveToDb(title, "")
                            isVisible = false
                            title = ""
                            navController.navigate("write_record")
                        }
                    ) {
                        Text("OK")
                    }
                },
                title = { Text("Memory Name") },
                text = {
                    Column() {
                        OutlinedTextField(
                            value = title,
                            onValueChange = {title = it},
                            label = { Text("Title") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            )
        }
    }
}

fun getTitles(callback: (List<String>) -> Unit){
    val database = FirebaseDatabase.getInstance()
    val databaseRef = database.reference.child("memory")
    var titles = mutableListOf<String>()

    databaseRef.addValueEventListener(object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            titles.clear()
            for(childSnapshot in snapshot.children) {
                val title = childSnapshot.key
                if(title != null) {
                    titles.add(title)
                }
            }
            callback(titles)
        }

        override fun onCancelled(error: DatabaseError) {
            callback(emptyList())
        }

    })
}