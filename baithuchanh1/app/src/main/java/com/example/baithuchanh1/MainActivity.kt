package com.example.baithuchanh1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreen()
        }
    }
}

@Composable
fun ProfileScreen() {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Tiêu đề
        Text(
            text = "THỰC HÀNH 01",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Khung nhập dữ liệu
        Box(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Column {
                // Ô nhập Họ và Tên
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Họ và tên") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Ô nhập Tuổi
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Tuổi") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    )

                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nút kiểm tra
        Button(
            onClick = {
                result = classifyAge(age.toIntOrNull() ?: -1)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Kiểm tra", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị kết quả phân loại nhóm tuổi
        if (result.isNotEmpty()) {
            Text(text = "Kết quả: $result", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// Hàm phân loại nhóm tuổi
fun classifyAge(age: Int): String {
    return when {
        age > 65 -> "Người già"
        age in 6..65 -> "Người lớn"
        age in 2..6 -> "Trẻ em"
        age in 0..2 -> "Em bé"
        else -> "Tuổi không hợp lệ"
    }
}
