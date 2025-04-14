package com.example.bai1

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.bai1.ui.theme.Bai1Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Khởi tạo DataStore ở cấp độ toàn cục
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Bai1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ThemeSelectionScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun ThemeSelectionScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val BACKGROUND_COLOR_KEY = longPreferencesKey("background_color")
    val coroutineScope = rememberCoroutineScope()

    // Đọc màu nền từ DataStore
    val backgroundColorFlow: Flow<Color> = context.dataStore.data.map { preferences ->
        val colorLong = preferences[BACKGROUND_COLOR_KEY] ?: Color.White.value.toLong()
        Color(colorLong.toULong()) // Chuyển đổi thành ULong để xử lý chính xác giá trị màu
    }

    // State để giữ màu nền đã lưu
    val savedBackgroundColor by backgroundColorFlow.collectAsState(initial = Color.White)

    // State để giữ màu nền xem trước
    var previewBackgroundColor by remember { mutableStateOf(savedBackgroundColor) }

    // Cập nhật previewBackgroundColor khi savedBackgroundColor thay đổi
    LaunchedEffect(savedBackgroundColor) {
        previewBackgroundColor = savedBackgroundColor
    }

    val colorOptions = listOf(
        Color(0xFFADD8E6), // Light Blue
        Color(0xFFFF69B4), // Hot Pink
        Color(0xFF333333)  // Dark Gray
    )

    Column(
        modifier = modifier
            .background(previewBackgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Setting",
            color = Color(0xFF2196F3),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Choosing the right theme sets the tone and personality of your app",
            color = Color(0xFF2196F3),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            colorOptions.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(color, shape = RoundedCornerShape(8.dp))
                        .clickable {
                            previewBackgroundColor = color
                        }
                )
            }
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    context.dataStore.edit { preferences ->
                        preferences[BACKGROUND_COLOR_KEY] = previewBackgroundColor.value.toLong()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            ),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .width(120.dp)
                .height(48.dp)
        ) {
            Text(
                text = "Apply",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}