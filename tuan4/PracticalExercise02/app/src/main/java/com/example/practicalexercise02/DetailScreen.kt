package com.example.practicalexercise02

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// DetailScreen: Màn hình chi tiết của một task
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, task: Task) {
    Scaffold(
        topBar = {
            // TopAppBar: Thanh trên cùng với tiêu đề, nút Back và nút Delete
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Detail",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF3A86FF)
                            )
                        )
                    }
                },
                navigationIcon = {
                    // Nút Back: Quay lại màn hình trước
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back",
                            tint = Color(0xFF3A86FF),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    // Nút Delete: Xóa task
                    IconButton(onClick = {
                        Log.d("DetailScreen", "Delete task: ${task.title}")
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.trash),
                            contentDescription = "Delete",
                            tint = Color(0xFFFFA500),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Tiêu đề và mô tả của task
                Text(
                    text = task.title,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = task.description,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Category, Status, Priority: Hiển thị thông tin phân loại của task
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFFE6E6),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Category: Danh mục của task
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.category),
                                contentDescription = "Category",
                                tint = Color.Black,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Category",
                                style = TextStyle(fontSize = 14.sp, color = Color.Black)
                            )
                            Text(
                                text = task.category,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        // Status: Trạng thái của task
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.tasklist),
                                contentDescription = "Status",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Status",
                                style = TextStyle(fontSize = 14.sp, color = Color.Black)
                            )
                            Text(
                                text = task.status,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color(0xFF4CAF50),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        // Priority: Mức độ ưu tiên của task
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.star),
                                contentDescription = "Priority",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Priority",
                                style = TextStyle(fontSize = 14.sp, color = Color.Black)
                            )
                            Text(
                                text = task.priority,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Subtasks: Danh sách các subtask của task
                Text(
                    text = "Subtasks",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                task.subtasks.forEach { subtask ->
                    SubtaskItem(subtask = subtask)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Attachment: Hiển thị file đính kèm nếu có
                if (task.fileName != null) {
                    Text(
                        text = "Attachment",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.attachment),
                                contentDescription = "Attachment",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = task.fileName,
                                style = TextStyle(fontSize = 14.sp, color = Color.Black)
                            )
                        }
                    }
                }
            }
        }
    )
}

// SubtaskItem: Thành phần hiển thị một subtask
@Composable
fun SubtaskItem(subtask: Subtask) {
    var isCompleted by remember { mutableStateOf(subtask.isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF5F5F5),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox để đánh dấu subtask hoàn thành
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { newValue ->
                    isCompleted = newValue
                },
                modifier = Modifier.size(24.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF3A86FF),
                    uncheckedColor = Color(0xFF3A86FF),
                    checkmarkColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Tiêu đề của subtask
            Text(
                text = subtask.title,
                style = TextStyle(fontSize = 14.sp, color = Color.Black)
            )
        }
    }
}