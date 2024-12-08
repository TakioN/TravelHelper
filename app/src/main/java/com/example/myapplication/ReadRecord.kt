package com.example.myapplication

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter

@Composable
fun ReadRecord(navController: NavController) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

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
            .padding(16.dp),
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
            text = "[행복했던 도쿄여행]",
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
//        Text(text = "첫날: 오사카성 공원을 산책하며 일본 전통 건축의 웅장함과 공원의 평화로운 분위기를 만끽했다.\n" +
//                "둘째 날: 도톤보리 강가를 따라 걷고, 구리코 간판 앞에서 사진을 찍으며 활기찬 거리를 즐겼다.\n" +
//                "셋째 날: 유니버설 스튜디오 재팬에서 하루 종일 다양한 어트랙션을 즐기며 동심으로 돌아갔다.\n" +
//                "넷째 날: 교토로 당일치기 여행을 떠나, 후시미이나리의 붉은 도리이 길을 걸으며 일본 전통 문화를 느꼈다.\n" +
//                "마지막 날: 공항으로 가기 전 난바 시장에서 오코노미야키와 타코야키를 맛보며 일본 음식의 매력을 다시 한번 느꼈다.",
//            modifier = Modifier
//                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
//                .padding(12.dp),
//            color = Color.Black
//        )
        Text(text = "첫날: 오사카성 공원을 산책하며 일본 전통 건축의 웅장함과 공원의 평화로운 분위기를 만끽했다.\n" +
                "둘째 날: 도톤보리 강가를 따라 걷고, 구리코 간판 앞에서 사진을 찍으며 활기찬 거리를 즐겼다.\n" +
                "셋째 날: 유니버설 스튜디오 재팬에서 하루 종일 다양한 어트랙션을 즐기며 동심으로 돌아갔다.\n" +
                "넷째 날: 교토로 당일치기 여행을 떠나, 후시미이나리의 붉은 도리이 길을 걸으며 일본 전통 문화를 느꼈다.\n" +
                "마지막 날: 공항으로 가기 전 난바 시장에서 오코노미야키와 타코야키를 맛보며 일본 음식의 매력을 다시 한번 느꼈다.",
            modifier = Modifier
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(12.dp),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 사진 추가 버튼 및 미리보기
        Button(
            onClick = { navController.navigate("write_record") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("수정")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 저장 버튼
        Button(
            onClick = { navController.popBackStack(route = "record", inclusive = false) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("확인")
        }
    }
}
